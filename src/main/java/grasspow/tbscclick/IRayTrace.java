package grasspow.tbscclick;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public interface IRayTrace {

    boolean isBlockTrace();

    boolean isEntityTrace();

    boolean isMissType();

    boolean isEmptyBlock(Minecraft minecraft);

    void leftClickBlock(Minecraft minecraft);

    void leftClickEntity(Minecraft minecraft);

    boolean isLookingAtEntity(Minecraft minecraft);

    BlockHitResult getBlockTrace();

    EntityHitResult getEntityTrace();

    HitResult getHitResult();
}