package grasspow.tbscclick;

import grasspow.tbscclick.impl.TbscClick;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Compat {
    private final IClick clicker;
    private final Minecraft mc;

    // Auto clicking
    private boolean autoLeft = false;
    private boolean autoRight = false;
    private boolean smartAttack = false;
    private boolean holdRight = false;
    private boolean autoCrouch = false;

    // Cooldowns
    private int leftClickCounter = 0;
    private int rightClickDelayTimer = 0;
    private int ticksSinceLastClick = 0;

    // Speed settings
    private int speedSetting = 5;

    public Compat(IClick clicker) {
        this.clicker = clicker;
        this.mc = clicker.getMinecraft();
    }

    public void onTick() {
        if (clicker.isGamePaused()) {
            return;
        }

        if (!clicker.isInGame() && !clicker.isInPauseMenu()) {
            resetState();
            return;
        }

        handleCooldowns();
        handleAutoCrouch();
        handleClicking();
    }

    private void resetState() {
        autoLeft = false;
        autoRight = false;
        smartAttack = false;
        holdRight = false;
        autoCrouch = false;
    }

    private void handleCooldowns() {
        if (leftClickCounter > 0) {
            leftClickCounter--;
        }

        if (rightClickDelayTimer > 0) {
            rightClickDelayTimer--;
        }

        ticksSinceLastClick++;
    }

    private void handleAutoCrouch() {
        if (autoCrouch && mc.player != null) {
            if (!mc.player.isCrouching()) {
                mc.player.setShiftKeyDown(true);
            }
        } else if (mc.player != null && mc.player.isCrouching()) {
            mc.player.setShiftKeyDown(false);
        }
    }

    private void handleClicking() {
        if (autoLeft) {
            handleAutoLeftClick();
        }

        if (autoRight || holdRight) {
            handleAutoRightClick();
        }
    }

    private void handleAutoLeftClick() {
        if (mc.player == null || mc.gameMode == null) {
            return;
        }

        // Check if attack key is pressed manually
        if (mc.options.keyAttack.isDown()) {
            disableAutoLeftByConflict();
            return;
        }

        // Smart attack logic
        if (smartAttack) {
            float cooldown = clicker.getSmartAttackCooldown();
            if (cooldown < 1.0f) {
                return;
            }
        }

        // Cooldown check
        if (leftClickCounter > 0) {
            return;
        }

        // Minimum interval check
        if (ticksSinceLastClick < clicker.getMinTicksBetweenClicks()) {
            return;
        }

        // Perform left click
        // 在1.20.1中不需要实现
        if (mc.hitResult != null) {
            switch (mc.hitResult.getType()) {
                case BLOCK:
                    leftClickBlock(mc.hitResult);
                    break;
                case ENTITY:
                    leftClickEntity(mc.hitResult);
                    break;
                case MISS:
                    mcReflLeftClick();
                    break;
            }
        } else {
            mcReflLeftClick();
        }

        // 在1.20.1中不需要实现
        ticksSinceLastClick = 0;
        // 在1.20.1中移除此行代码
    }

    private void handleAutoRightClick() {
        if (mc.player == null || mc.gameMode == null) {
            return;
        }

        // Check if use key is pressed manually
        if (mc.options.keyUse.isDown()) {
            if (autoRight) {
                autoRight = false;
            }
            if (holdRight) {
                holdRight = false;
                clicker.setHoldButton(clicker.getUseKey(), false);
            }
            return;
        }

        // Cooldown check
        if (rightClickDelayTimer > 0) {
            return;
        }

        // Minimum interval check
        if (ticksSinceLastClick < clicker.getMinTicksBetweenClicks()) {
            return;
        }

        // Perform right click
        if (holdRight) {
            clicker.setHoldButton(clicker.getUseKey(), true);
        } else {
            mcReflRightClick();
            rightClickDelayTimer = 4;
            ticksSinceLastClick = 0;
        }
    }

    private void leftClickBlock(HitResult hitResult) {
        if (!(hitResult instanceof BlockHitResult blockHitResult) || mc.player == null || mc.gameMode == null) {
            return;
        }

        BlockPos blockPos = blockHitResult.getBlockPos();
        if (mc.level == null) {
            return;
        }

        BlockState blockState = mc.level.getBlockState(blockPos);
        boolean isEmpty = blockState.isAir() || !blockState.canOcclude();

        if (!isEmpty) {
            mc.gameMode.startDestroyBlock(blockPos, blockHitResult.getDirection());
            mc.player.swing(InteractionHand.MAIN_HAND);
        } else {
            mcReflLeftClick();
        }
    }

    private void leftClickEntity(HitResult hitResult) {
        if (!(hitResult instanceof EntityHitResult entityHitResult) || mc.player == null || mc.gameMode == null) {
            return;
        }

        Entity entity = entityHitResult.getEntity();
        if (entity != null) {
            mc.gameMode.attack(mc.player, entity);
            mc.player.swing(InteractionHand.MAIN_HAND);
        } else {
            mcReflLeftClick();
        }
    }

    public void onInitGuiPre() {
        // Reset held keys when GUI is opened/closed
        if (holdRight) {
            clicker.setHoldButton(clicker.getUseKey(), false);
        }
    }

    public void onKeyPressed() {
        if (mc.screen != null) {
            return;
        }

        if (clicker.getToggleRightKey().isDown()) {
            toggleAutoRight();
        }

        if (clicker.getToggleLeftKey().isDown()) {
            toggleAutoLeft();
        }

        if (clicker.getToggleSmartAttackKey().isDown()) {
            toggleSmartAttack();
        }

        if (clicker.getToggleHoldRightKey().isDown()) {
            toggleHoldRight();
        }

        if (clicker.getSpeedKey().isDown()) {
            adjustSpeed();
        }

        if (clicker.getCrouchKey().isDown()) {
            toggleAutoCrouch();
        }
    }

    private void toggleAutoRight() {
        autoRight = !autoRight;
        if (!autoRight) {
            clicker.sendMessage("Disabled auto right click");
        } else {
            clicker.sendMessage("Enabled auto right click");
        }
    }

    private void toggleAutoLeft() {
        autoLeft = !autoLeft;
        if (!autoLeft) {
            clicker.sendMessage("Disabled auto left click");
        } else {
            clicker.sendMessage("Enabled auto left click");
        }
    }

    private void toggleSmartAttack() {
        smartAttack = !smartAttack;
        if (!smartAttack) {
            clicker.sendMessage("Disabled smart attack");
        } else {
            clicker.sendMessage("Enabled smart attack");
        }
    }

    private void toggleHoldRight() {
        holdRight = !holdRight;
        if (!holdRight) {
            clicker.setHoldButton(clicker.getUseKey(), false);
            clicker.sendMessage("Disabled hold right click");
        } else {
            clicker.sendMessage("Enabled hold right click");
        }
    }

    private void adjustSpeed() {
        speedSetting = (speedSetting % 10) + 1;
        int newTicks = Math.max(clicker.getMinTicksBetweenClicks(),
                clicker.getMaxTicksBetweenClicks() - (speedSetting - 1) * clicker.getTicksStepBetweenClicks());
        
        try {
            Field leftClickCounterField = ObfuscationReflectionHelper.findField(Minecraft.class, clicker.getLeftClickCounterFieldMapping());
            Field rightClickDelayTimerField = ObfuscationReflectionHelper.findField(Minecraft.class, clicker.getRightClickDelayTimerFieldMapping());
            
            leftClickCounterField.setAccessible(true);
            rightClickDelayTimerField.setAccessible(true);
            
            leftClickCounterField.set(mc, 0);
            rightClickDelayTimerField.set(mc, 0);
        } catch (Exception ignored) {
        }
        
        clicker.sendMessage("Speed set to " + speedSetting + "/10");
    }

    private void toggleAutoCrouch() {
        autoCrouch = !autoCrouch;
        if (!autoCrouch && mc.player != null) {
            mc.player.setShiftKeyDown(false);
        }
        if (!autoCrouch) {
            clicker.sendMessage("Disabled auto crouch");
        } else {
            clicker.sendMessage("Enabled auto crouch");
        }
    }

    public void onRenderGameOverlay() {
        StringBuilder displayText = new StringBuilder();
        if (autoRight) displayText.append("[R]");
        if (autoLeft) displayText.append("[L]");
        if (smartAttack) displayText.append("[S]");
        if (holdRight) displayText.append("[H]");
        if (autoCrouch) displayText.append("[C]");

        if (displayText.length() > 0) {
            clicker.renderTextOnScreen(displayText.toString(), 2, 2, 0xFFFFFF);
        }
    }

    public void disableAutoLeftByConflict() {
        if (autoLeft) {
            autoLeft = false;
            clicker.sendMessage("Disabled auto left click due to conflict");
        }
    }

    private void mcReflRightClick() {
        try {
            Method rightClickMethod = ObfuscationReflectionHelper.findMethod(Minecraft.class, clicker.getRightClickMouseMethodMapping());
            rightClickMethod.setAccessible(true);
            rightClickMethod.invoke(mc);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
    }

    private void mcReflLeftClick() {
        try {
            Method leftClickMethod = ObfuscationReflectionHelper.findMethod(Minecraft.class, clicker.getLeftClickMouseMethodMapping());
            leftClickMethod.setAccessible(true);
            leftClickMethod.invoke(mc);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
    }
}