package com.fireball_stick.sticks_click_block;

import com.fireball_stick.customFunctions.tnt.CustomTnt;
import com.fireball_stick.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class TNTStickUnboundClickBlock {

    //Hits a block
    public static InteractionResult use(Item item, Level level, Player player, InteractionHand hand)  {
        /*
        BlockPlaceContext placeContext = new BlockPlaceContext(context);
        BlockPos clickedPos = placeContext.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();
         */

        if (level instanceof ServerLevel serverLevel && player != null && !level.isClientSide()) {
            int spawnHeight = 30;
            int reach = 1000;
            /*
            double xDir = clickedPos.getX();
            double yDir = clickedPos.getY();
            double zDir = clickedPos.getZ();
             */
            int tntAmount = 100;
            //Makes the start spawn angle of the TNT be equal to the direction the player is facing (default (0): east)
            final double[] angle = {Math.toRadians(player.getYRot() + 90)};
            double angleStep = Math.PI / ((double) tntAmount / 2); //How smooth the curve looks
            double amplitude = 15; //Width of the curve
            //Can be replaced with a hardcoded float instead, since all the primedTNTs spawn at the same time
            //int tntFuseTimer = (tntAmount * 50) / 50 ; //50 ms = 1 tick
            Vec3 playerEyeStart = player.getEyePosition();
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
            final double[] changePosition = {0}; //Initial position of the starting TNT
            for (int i = 0; i < tntAmount; i++) {
                int finalI = i;
                    //Creates primed TNTs every iteration
                    CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
                    //X dir: cos, Z dir: sin, makes a circle
                    customTnt.setPos(target.getX() + (Math.cos(angle[0]) * amplitude),
                            target.getY() + spawnHeight,
                            target.getZ() + (Math.sin(angle[0]) * amplitude));
                    customTnt.setFuse(200);
                    customTnt.setExplosionPower(15.0F);
                    customTnt.setExplodeOnContact(true);
                    customTnt.setDefaultGravity(0.04);
                    //Adds the primed TNT to the world
                    serverLevel.addFreshEntity(customTnt);
                    //Changes the initial angle by the value of angleStep every iteration so the TNTs are not static
                    angle[0] += angleStep;
                    //Height of the cos curve every iteration
                    changePosition[0] += Math.PI / ((double) (tntAmount / 4) / 2);
            }
            //Plays a sound when a block is clicked
            level.playSound(null,
                    target.getX(),
                    target.getY() + spawnHeight,
                    target.getZ(),
                    SoundEvents.TNT_PRIMED,
                    SoundSource.PLAYERS,
                    0.4F,
                    1.0F);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }
}
