package com.explosion_wands.wands;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class FireballBarrageWand {
    private static final List<Runnable> QUEUE = new ArrayList<>();
    private static int tickCounter = 0;
    private static int taskCount = 0;
    private static final Queue<Runnable> nextTickQueue = new ArrayDeque<>();

    public static void add(Runnable task) {
        QUEUE.add(task);
    }

    //Tick queue system
    public static void tick() {
        if (QUEUE.isEmpty()) {
            return;
        }
        tickCounter++;
        if (tickCounter >= 1) {
            //Resets the tick counter
            tickCounter = 0;
            QUEUE.remove(0).run();
        }
    }

    //Hits a block
    public static InteractionResult use(Item item, Level level, Player player, InteractionHand hand) {
        int reach = 360;
        int spawnHeight = 50;
        double amplitude = 15;
        int fireballAmount = 40;
        int explosionPower = 10;
        //Direction the fireballs will head towards, and the speed of the fireballs
        double xDir = 0;
        double yDir = -2;
        double zDir = 0;

        if(player != null && level instanceof ServerLevel serverLevel) {
            Vec3 dir = new Vec3(xDir, yDir, zDir);
            double angle = Math.toRadians(player.getYRot() + 90);
            //Makes the fireballs equally spread out
            double angleStep = Math.PI / ((double) fireballAmount / 2);
            Vec3 playerEyeStart = player.getEyePosition();
            //Also how far away the fireballs spawn from the player
            Vec3 playerLookAngle = player.getLookAngle();
            Vec3 playerEyeEnd = playerEyeStart.add(playerLookAngle.scale(reach));
            BlockHitResult blockHitResult = level.clip(new ClipContext(
                    playerEyeStart,
                    playerEyeEnd,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player
            ));
            BlockPos target = blockHitResult.getBlockPos();
            for (int i = 0; i < fireballAmount; i++) {
                LargeFireball largeFireball = new LargeFireball(
                        level,
                        player,
                        dir,
                        explosionPower);
                largeFireball.setPos(
                        target.getX() + (Math.cos(angle) * amplitude),
                        target.getY() + spawnHeight,
                        target.getZ() + (Math.sin(angle) * amplitude));

                largeFireball.setDeltaMovement(xDir, yDir, zDir);
                largeFireball.addTag("fireball");
                serverLevel.addFreshEntity(largeFireball);
                if(largeFireball.touchingUnloadedChunk()) {
                    largeFireball.discard();
                }
                angle += angleStep;
            }
            serverLevel.playSound(null, blockHitResult.getBlockPos().getX(),
                    //Makes the sound play as close to the y direction the player is at
                    blockHitResult.getBlockPos().getY() + spawnHeight,
                    blockHitResult.getBlockPos().getZ(),
                    SoundEvents.FIRECHARGE_USE,
                    SoundSource.PLAYERS,
                    0.4F,
                    1.0F);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}
