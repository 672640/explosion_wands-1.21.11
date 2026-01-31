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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class TNTStickFallingClickBlock {

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
            ConcretePowderBlock powderBlock = new ConcretePowderBlock(Blocks.BLACK_CONCRETE, BlockBehaviour.Properties.of());
            String entityType = "";
            BlockPos target = blockHitResult.getBlockPos();
            //Failsafe in-case we spawn more entities than is intended
            if(spawnedEntities <= maxEntities) {
                for (double theta = ExplosionEntities.theta; theta <= lessThanTheta; theta += incrementTheta) {
                    for (double phi = ExplosionEntities.phi; phi <= lessThanPhi; phi += incrementPhi) {
                        if(randomEntity <= spawnedEntities / 8 && spawnedEntities >= 0) {
                            powderBlock = new ConcretePowderBlock(Blocks.GRAY_CONCRETE_POWDER, BlockBehaviour.Properties.of());
                            entityType = entityToSpawn.toString();
                        }
                        if(randomEntity <= (spawnedEntities / 4) && randomEntity > (spawnedEntities / 8)) {
                            powderBlock = new ConcretePowderBlock(Blocks.BLACK_CONCRETE, BlockBehaviour.Properties.of());
                            entityType = entityToSpawn.toString();
                        }
                        if(randomEntity <= (spawnedEntities / 8) * 2 + (spawnedEntities / 8) && randomEntity > (spawnedEntities / 4)) {
                            powderBlock = new ConcretePowderBlock(Blocks.YELLOW_CONCRETE_POWDER, BlockBehaviour.Properties.of());
                            entityType = entityToSpawn.toString();
                        }
                        if(randomEntity <= spawnedEntities / 2 && randomEntity > (spawnedEntities / 8) * 2 + (spawnedEntities / 8)) {
                            powderBlock = new ConcretePowderBlock(Blocks.PURPLE_CONCRETE_POWDER, BlockBehaviour.Properties.of());
                            entityType = entityToSpawn.toString();
                        }
                        if(randomEntity <= (spawnedEntities / 2) + (spawnedEntities / 8) && randomEntity > (spawnedEntities / 2)) {
                            powderBlock = new ConcretePowderBlock(Blocks.GREEN_CONCRETE_POWDER, BlockBehaviour.Properties.of());
                            entityType = entityToSpawn.toString();
                        }
                        if(randomEntity <= (spawnedEntities / 2) + (spawnedEntities / 4) && randomEntity > (spawnedEntities / 2) + (spawnedEntities / 8)) {
                            powderBlock = new ConcretePowderBlock(Blocks.WHITE_CONCRETE_POWDER, BlockBehaviour.Properties.of());
                            entityType = entityToSpawn.toString();
                        }
                        if(randomEntity <= spawnedEntities - (spawnedEntities / 8) && randomEntity > (spawnedEntities / 2) + (spawnedEntities / 4)) {
                            powderBlock = new ConcretePowderBlock(Blocks.LIGHT_BLUE_CONCRETE_POWDER, BlockBehaviour.Properties.of());
                            entityType = entityToSpawn.toString();
                        }
                        if(randomEntity <= spawnedEntities && randomEntity > spawnedEntities - (spawnedEntities / 8)) {

                            entityType = entityToSpawn.toString();
                        }
                        CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
                        //This does not make a perfect circle, but it should not be noticeable
                        if (increment <= randomExplosion && customTnt != null) {
                            ConcretePowderBlock(Blocks.RED_CONCRETE_POWDER, BlockBehaviour.Properties.of()
                            serverLevel.addFreshEntity(customTnt);
                            customTnt.setFuse(fuse);
                            customTnt.setExplosionPower(randomIncrement);
                        }
                        if(entity != null) {
                            entity.setPos(target.getX() + x,
                                    target.getY() + y + spawnHeight,
                                    target.getZ() + z
                            );
                        }
                        serverLevel.addFreshEntity(entity);
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
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }
}
//Make there be a random chance to spawn different falling block types, such as sand, gravel and the colored concrete powders
//Maybe make it have different blocks in the same use
//Fallback on how many falling blocks can be spawned