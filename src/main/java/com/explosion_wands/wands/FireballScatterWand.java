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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class FireballScatterWand {

    //Hits a block
    public static InteractionResult use(Item item, Level level, Player player, InteractionHand hand)  {

        if (level instanceof ServerLevel serverLevel && player != null && !level.isClientSide()) {
            int maxEntities = ExplosionEntities.maxEntities;
            int fuse = ExplosionEntities.fuse;
            int spawnedEntities = ExplosionEntities.spawnedEntities;
            RandomSource random = RandomSource.create();
            double maxRandomPos = ExplosionEntities.randomPos;
            double randomPos = (maxRandomPos + random.nextDouble() * (maxRandomPos - 0));
            int fireballExplosionPower = 8;
            int increment = ExplosionEntities.increment;
            double lessThanTheta = ExplosionEntities.lessThanTheta;
            double lessThanPhi = ExplosionEntities.lessThanPhi;
            double incrementTheta;
            incrementTheta = 0.5;
            double incrementPhi;
            incrementPhi = 0.5;
            double x = ExplosionEntities.x;
            double y = ExplosionEntities.y;
            double z = ExplosionEntities.z;
            double r;
            r = 8;
            int spawnHeight;
            spawnHeight = 20;
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
                for (double theta = ExplosionEntities.theta; theta <= lessThanTheta / 2; theta += incrementTheta) {
                    for (double phi = ExplosionEntities.phi; phi <= lessThanPhi; phi += incrementPhi) {
                        LargeFireball fireball = new LargeFireball(level, player, playerLookAngle, fireballExplosionPower);
                        CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
                        //This does not make a perfect circle, but it should not be noticeable
                        if (increment <= 0 && customTnt != null) {
                            customTnt.setPos(target.getX(),
                                    target.getY() + spawnHeight - 3,
                                    target.getZ()
                            );
                            serverLevel.addFreshEntity(customTnt);
                            customTnt.setFuse(fuse);
                            customTnt.setExplosionPower(0F);
                            //System.out.println("TNTs spawned: " + (increment + 1));
                        }
                        //Creates fireball every iteration
                        //X dir: cos, Z dir: sin, makes a circle
                        if(x != 0 && y != 0 && z != 0) {
                            fireball.setPos(target.getX() + x,
                                    target.getY() - y + spawnHeight,
                                    target.getZ() - z

                            );
                            fireball.addTag("fireball");
                            serverLevel.addFreshEntity(fireball);
                        } else {
                            fireball.discard();
                        }
                        //Changes the initial angle by the value of angleStep every iteration so the TNTs are not static
                        //Height of the cos curve every iteration
                        x = r * Math.sin(theta) * Math.cos(phi) + randomPos;
                        y = r * Math.cos(theta) + randomPos;
                        z = r * Math.sin(theta) * Math.sin(phi) + randomPos;
                        increment++;
                    }
                }
            }
            /*
            System.out.println(
                      "Pre-calculated entities:   " + spawnedEntities
                    + ",   entities:   " + increment
                    + ",   random explosion:   " + randomExplosion
                    + ",   random increment:   " + randomIncrement
            );
             */
            /*
            System.out.println(
                    ",   random entity number:    " + randomEntity
                    + ",   entity type: " + entityType
            );
             */
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