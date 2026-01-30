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
            float randomExplosion = (minExplosion + random.nextFloat() * (maxExplosion - minExplosion));
            int randomIncrement = minIncrement + random.nextInt(maxIncrement - minIncrement);
            int randomEntity = minRandomEntities + random.nextInt(maxRandomEntities - minRandomEntities);

            int increment = ExplosionEntities.increment;
            double lessThanTheta = ExplosionEntities.lessThanTheta;
            double lessThanPhi = ExplosionEntities.lessThanPhi;
            double incrementTheta = ExplosionEntities.incrementTheta;
            double incrementPhi = ExplosionEntities.incrementPhi;
            double x = ExplosionEntities.x;
            double y = ExplosionEntities.y;
            double z = ExplosionEntities.z;
            double r = ExplosionEntities.r;
            int spawnHeight = ExplosionEntities.spawnHeight;
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
            //Failsafe in-case we spawn more entities than is intended
            if(spawnedEntities <= maxEntities) {
                for (double theta = ExplosionEntities.theta; theta <= lessThanTheta; theta += incrementTheta) {
                    for (double phi = ExplosionEntities.phi; phi <= lessThanPhi; phi += incrementPhi) {
                        if(randomEntity <= spawnedEntities / 4 && spawnedEntities >= 0) {
                            entityToSpawn = EntityType.TNT;
                            entityType = entityToSpawn.toString();
                        }
                        if(randomEntity <= spawnedEntities / 2 && randomEntity > spawnedEntities / 4) {
                            entityToSpawn = EntityType.FIREBALL;
                            entityType = entityToSpawn.toString();
                        }
                        if(randomEntity <= (spawnedEntities / 4) * 3 && randomEntity > spawnedEntities / 2) {
                            entityToSpawn = EntityType.TNT;
                            entityType = entityToSpawn.toString();
                        }
                        if(randomEntity <= spawnedEntities && randomEntity > (spawnedEntities / 4) * 3) {
                            entityToSpawn = EntityType.FIREBALL;
                            entityType = entityToSpawn.toString();
                        }
                        Entity entity = entityToSpawn.create(level, EntitySpawnReason.TRIGGERED);
                        CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
                        //This does not make a perfect circle, but it should not be noticeable
                        if (increment <= randomExplosion) {
                            customTnt.setPos(target.getX(),
                                    target.getY() + spawnHeight,
                                    target.getZ()
                            );
                            serverLevel.addFreshEntity(customTnt);
                            customTnt.setFuse(fuse);
                            customTnt.setExplosionPower(randomIncrement);
                        }
                        //Creates primed TNTs every iteration
                        //CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
                        //X dir: cos, Z dir: sin, makes a circle
                        entity.setPos(target.getX() + x,
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
                        serverLevel.addFreshEntity(entity);
                        //Changes the initial angle by the value of angleStep every iteration so the TNTs are not static
                        //Height of the cos curve every iteration
                        //changePosition[0] += Math.PI / ((double) (tntAmount / 4) / 2);
                        x = r * Math.sin(theta) * Math.cos(phi);
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
                    + ",   random increment:   " + randomIncrement
            );

            System.out.println(
                    ",   random entity number:    " + randomEntity
                    + ",   entity type: " + entityType
            );
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
//Maybe split them into TNT and fireball
//Customize the fireballs with the same technique as for the other fireball classes
//Replace normal primedTnt with customTnt