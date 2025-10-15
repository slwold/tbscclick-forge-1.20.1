package grasspow.tbscclick.impl;

import grasspow.tbscclick.IRayTrace;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class RayTrace implements IRayTrace {
    private final HitResult hitResult;

    public RayTrace(HitResult hitResult) {
        this.hitResult = hitResult;
    }

    @Override
    public boolean isBlockTrace() {
        return hitResult instanceof BlockHitResult;
    }

    @Override
    public boolean isEntityTrace() {
        return hitResult instanceof EntityHitResult;
    }

    @Override
    public boolean isMissType() {
        return hitResult.getType() == HitResult.Type.MISS;
    }

    @Override
    public boolean isEmptyBlock(Minecraft minecraft) {
        if (hitResult instanceof BlockHitResult blockHitResult) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockState blockState = minecraft.level.getBlockState(blockPos);
            Block block = blockState.getBlock();
            return block.defaultBlockState().isAir();
        }
        return false;
    }

    @Override
    public void leftClickBlock(Minecraft minecraft) {
        if (hitResult instanceof BlockHitResult blockHitResult) {
            minecraft.gameMode.startDestroyBlock(blockHitResult.getBlockPos(), blockHitResult.getDirection());
        }
    }

    @Override
    public void leftClickEntity(Minecraft minecraft) {
        if (hitResult instanceof EntityHitResult entityHitResult) {
            Entity entity = entityHitResult.getEntity();
            if (entity instanceof Player) {
                if (minecraft.player != null) {
    minecraft.gameMode.attack(minecraft.player, entity);
}
            }
        }
    }

    @Override
    public boolean isLookingAtEntity(Minecraft minecraft) {
        if (hitResult instanceof EntityHitResult entityHitResult) {
            return entityHitResult.getEntity() != null;
        }
        return false;
    }

    @Override
    public BlockHitResult getBlockTrace() {
        if (hitResult instanceof BlockHitResult blockHitResult) {
            return blockHitResult;
        }
        return null;
    }

    @Override
    public EntityHitResult getEntityTrace() {
        if (hitResult instanceof EntityHitResult entityHitResult) {
            return entityHitResult;
        }
        return null;
    }

    @Override
    public HitResult getHitResult() {
        return hitResult;
    }
}