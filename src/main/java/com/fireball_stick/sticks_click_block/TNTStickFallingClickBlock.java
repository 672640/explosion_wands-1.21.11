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

public class TNTStickFallingClickBlock {

    //Hits a block
    public static InteractionResult use(Item item, Level level, Player player, InteractionHand hand)  {

        if (level instanceof ServerLevel serverLevel && player != null && !level.isClientSide()) {
            float minExplosion = ExplosionEntities.minExplosion;
            float maxExplosion = ExplosionEntities.maxExplosion;
            int minIncrement = ExplosionEntities.minIncrement;
            int maxIncrement = ExplosionEntities.maxIncrement;
            RandomSource random = RandomSource.create();
            float randomExplosion = (minExplosion + random.nextFloat() * (maxExplosion - minExplosion));
            int randomIncrement = minIncrement + random.nextInt(maxIncrement - minIncrement);
            int increment = ExplosionEntities.increment;
            double incrementTheta = ExplosionEntities.incrementTheta;
            double incrementPhi = ExplosionEntities.incrementPhi;
            double x = ExplosionEntities.x;
            double y = ExplosionEntities.y;
            double z = ExplosionEntities.z;
            double r = ExplosionEntities.r;
            int spawnHeight = ExplosionEntities.spawnHeight;
            int reach = ExplosionEntities.reach;
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
            BlockPos target = blockHitResult.getBlockPos();
            for (double theta = ExplosionEntities.theta; theta <= Math.PI; theta += incrementTheta) {
                for (double phi = ExplosionEntities.phi; phi <= 2 * Math.PI; phi += incrementPhi) {
                    EntityType<?> entityToSpawn = EntityType.FALLING_BLOCK;
                    Entity entity = entityToSpawn.create(level, EntitySpawnReason.TRIGGERED);
                    CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
                    if(increment <= randomIncrement) {
                        customTnt.setPos(target.getX(),
                                target.getY() + spawnHeight,
                                target.getZ()
                        );
                        serverLevel.addFreshEntity(customTnt);
                        customTnt.setFuse(0);
                        customTnt.setExplosionPower(randomExplosion);
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
                    //System.out.println("Entities spawned: " + increment);
                }
            }
            System.out.println("Random explosion: " + randomExplosion);
            System.out.println("Random increment: " + randomIncrement);
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
//Make there be a random chance to spawn different falling block types, such as sand, gravel and the colored concrete powders
//Maybe make it have different blocks in the same use
//Fallback on how many falling blocks can be spawned