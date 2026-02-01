package com.fireball_stick.sticks_click_block;

import com.fireball_stick.customFunctions.tnt.CustomTnt;
import com.fireball_stick.entity.ModEntities;
import com.fireball_stick.sharedValues.ExplosionEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class TNTFireballStickExplosionClickBlock {

    //Hits a block
    public static InteractionResult use(Item item, Level level, Player player, InteractionHand hand)  {

        if (level instanceof ServerLevel serverLevel && player != null && !level.isClientSide()) {
            int maxEntities = ExplosionEntities.maxEntities;
            int fuse = ExplosionEntities.fuse;
            int spawnedEntities = ExplosionEntities.spawnedEntities;
            float minExplosion = ExplosionEntities.minExplosion;
            float maxExplosion = ExplosionEntities.maxExplosion;
            int minIncrement = ExplosionEntities.minIncrement;
            int maxIncrement = ExplosionEntities.maxIncrement;
            int minRandomEntities = ExplosionEntities.minRandomEntity;
            int maxRandomEntities = ExplosionEntities.maxRandomEntity;
            RandomSource random = RandomSource.create();
            double maxRandomPos = ExplosionEntities.randomPos;
            double randomPos = (maxRandomPos + random.nextDouble() * (maxRandomPos - 0));
            float randomExplosion = (minExplosion + random.nextFloat() * (maxExplosion - minExplosion));
            int randomIncrement = minIncrement + random.nextInt(maxIncrement - minIncrement);
            int randomEntity = minRandomEntities + random.nextInt(maxRandomEntities - minRandomEntities);
            int fireballExplosionPower = 5;
            int increment = ExplosionEntities.increment;
            double lessThanTheta = ExplosionEntities.lessThanTheta;
            double lessThanPhi = ExplosionEntities.lessThanPhi;
            double incrementTheta = ExplosionEntities.incrementTheta;
            incrementTheta = 0.5;
            double incrementPhi = ExplosionEntities.incrementPhi;
            incrementPhi = 0.5;
            double x = ExplosionEntities.x;
            double y = ExplosionEntities.y;
            double z = ExplosionEntities.z;
            double r = ExplosionEntities.r;
            r = 3;
            int spawnHeight = ExplosionEntities.spawnHeight;
            spawnHeight = 25;
            int reach = ExplosionEntities.reach;
            //int spawnedEntitiesComparisonAmount = ExplosionEntities.spawnedEntitiesComparisonAmount;
            //int spawnedEntitiesComparison = ExplosionEntities.spawnedEntitiesComparison;
            //Makes the start spawn angle of the TNT be equal to the direction the player is facing (default (0): east)
            final double[] angle = {Math.toRadians(player.getYRot() + 90)};
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
            EntityType<?> entityToSpawn = EntityType.CHICKEN;
            String entityType = "";
            BlockPos target = blockHitResult.getBlockPos();
            //entityToSpawn = EntityType.FIREBALL;
            //Failsafe in-case we spawn more entities than is intended
            if(spawnedEntities <= maxEntities) {
                for (double theta = ExplosionEntities.theta; theta <= lessThanTheta; theta += incrementTheta) {
                    for (double phi = ExplosionEntities.phi; phi <= lessThanPhi; phi += incrementPhi) {
                        Entity entity = entityToSpawn.create(level, EntitySpawnReason.TRIGGERED);
                        LargeFireball fireball = new LargeFireball(level, player, playerLookAngle, fireballExplosionPower);
                        CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
                        //This does not make a perfect circle, but it should not be noticeable
                        if (increment <= 0 && customTnt != null) {
                            customTnt.setPos(target.getX(),
                                    target.getY() + spawnHeight + 6,
                                    target.getZ()
                            );
                            serverLevel.addFreshEntity(customTnt);
                            customTnt.setFuse(fuse);
                            customTnt.setExplosionPower(0F);
                            System.out.println("TNTs spawned: " + (increment + 1));
                        }
                        //Creates primed TNTs every iteration
                        //CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
                        //X dir: cos, Z dir: sin, makes a circle
                        fireball.setPos(target.getX() + x,
                                target.getY() + y + spawnHeight,
                                target.getZ() + z
                        );
                    /*
                    customTnt.setFuse(40);
                    customTnt.setExplosionPower(0F);
                    customTnt.setExplodeOnContact(false);
                    customTnt.setDefaultGravity(-0.04);

                     */
                        //Adds the primed TNT to the world
                        serverLevel.addFreshEntity(fireball);
                        //Changes the initial angle by the value of angleStep every iteration so the TNTs are not static
                        //Height of the cos curve every iteration
                        //changePosition[0] += Math.PI / ((double) (tntAmount / 4) / 2);
                        x = r * Math.sin(theta) * Math.cos(phi) + randomPos;
                        y = r * Math.cos(theta) + randomPos;
                        z = r * Math.sin(theta) * Math.sin(phi) + randomPos;
                        increment++;
                    }
                }
            }

            System.out.println(
                      "Pre-calculated entities:   " + spawnedEntities
                    + ",   entities:   " + increment
                    + ",   random explosion:   " + randomExplosion
                    + ",   random increment:   " + randomIncrement
            );

            System.out.println(
                    ",   random entity number:    " + randomEntity
                    + ",   entity type: " + entityType
            );
            //Plays a sound when a block is clicked
            /*
            level.playSound(null,
                    target.getX(),
                    target.getY() + spawnHeight,
                    target.getZ(),
                    SoundEvents.TNT_PRIMED,
                    SoundSource.PLAYERS,
                    0.4F,
                    1.0F);
             */
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }
}
//Maybe split them into TNT and fireball
//Customize the fireballs with the same technique as for the other fireball classes
//Maybe make it a half-circle instead