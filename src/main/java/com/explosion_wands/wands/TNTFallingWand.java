package com.explosion_wands.wands;

import com.explosion_wands.customFunctions.tnt.CustomTnt;
import com.explosion_wands.entity.ModEntities;
import com.explosion_wands.sharedValues.ExplosionEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class TNTFallingWand {

    //Hits a block
    public static InteractionResult use(Item item, Level level, Player player, InteractionHand hand)  {

        if (level instanceof ServerLevel serverLevel && player != null && !level.isClientSide()) {
            int maxEntities = ExplosionEntities.maxEntities;
            int fuse = ExplosionEntities.fuse;
            int spawnedEntities = ExplosionEntities.spawnedEntities;
            float minExplosion;
            minExplosion = 0.5F;
            float maxExplosion;
            maxExplosion = 4F;
            int minIncrement = ExplosionEntities.minIncrement;
            int maxIncrement = ExplosionEntities.maxIncrement;
            RandomSource random = RandomSource.create();
            float randomExplosion = (minExplosion + random.nextFloat() * (maxExplosion - minExplosion));
            int randomIncrement = minIncrement + random.nextInt(maxIncrement - minIncrement);
            int increment = ExplosionEntities.increment;
            double lessThanTheta = ExplosionEntities.lessThanTheta;
            double lessThanPhi = ExplosionEntities.lessThanPhi;
            double incrementTheta = ExplosionEntities.incrementTheta;
            double incrementPhi = ExplosionEntities.incrementPhi;
            double x = ExplosionEntities.x;
            double y = ExplosionEntities.y;
            double z = ExplosionEntities.z;
            double r = 1.5;
            int spawnHeight = ExplosionEntities.spawnHeight;
            int reach = ExplosionEntities.reach;
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
            //Failsafe in-case we spawn more entities than is intended
            if(spawnedEntities <= maxEntities) {
                for (double theta = ExplosionEntities.theta; theta <= lessThanTheta; theta += incrementTheta) {
                    for (double phi = ExplosionEntities.phi; phi <= lessThanPhi; phi += incrementPhi) {
                        //Adds the entities to the world
                        CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
                        CustomTnt customTnt2 = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
                        //This does not make a perfect circle, but it should not be noticeable
                            if (customTnt != null && increment <= 1) {
                                customTnt.setPos(target.getX(),
                                        target.getY() + spawnHeight,
                                        target.getZ()
                                );
                                customTnt.setFuse(fuse);
                                customTnt.setExplosionPower(randomIncrement);
                                serverLevel.addFreshEntity(customTnt);
                            }
                        if (customTnt2 != null) {
                            if (x != 0 && y != 0 && z != 0) {
                                customTnt2.setPos(target.getX() + x,
                                        target.getY() + y + spawnHeight,
                                        target.getZ() + z
                                );
                                customTnt2.setFuse(500);
                                customTnt2.setExplodeOnContact(true);
                                customTnt2.setExplosionPower(5F);
                                serverLevel.addFreshEntity(customTnt2);
                            } else {
                                customTnt2.discard();
                            }
                            x = r * Math.sin(theta) * Math.cos(phi) ;
                            y = r * Math.cos(theta);
                            z = r * Math.sin(theta) * Math.sin(phi);
                            increment++;
                        }
                    }
                }
                System.out.println(
                        "Pre-calculated entities:   " + spawnedEntities
                                + ",   entities:   " + increment
                                + ",   random explosion:   " + randomExplosion
                                + ",   random increment:   " + 1
                );
            //Plays a sound when a block is clicked
            /*
            level.playSound(null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.TNT_PRIMED,
                    SoundSource.PLAYERS,
                    0.4F,
                    1.0F);
             */
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }
}
