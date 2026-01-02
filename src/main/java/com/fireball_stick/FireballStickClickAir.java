package com.fireball_stick;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;

public class FireballStickClickAir extends Item {
    public FireballStickClickAir(Item.Properties properties) {
        super(properties);
    }
    //Initializes the item
    public static InteractionResult use(Item item, Level level, Player player, InteractionHand hand) {
        //ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (hitResult.getType() != Type.BLOCK && !level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }

    public static Projectile asProjectile(Item item, Level level, Player player, InteractionHand hand) {
        //ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        //Reach for hitting an entity
        int reach = 10;
        int explosionPowerAir = 10;
        int explosionPowerEntity = 1;
        //fireball's velocity
        int velocity = 10;
        double dirX = player.getX();
        double dirY = player.getY();
        double dirZ = player.getZ();
        //Vec3 dir = new Vec3(dirX, dirY, dirZ);
        Vec3 playerLookDir = player.getLookAngle();
        Vec3 playerStartDir = player.getEyePosition();
        Vec3 playerEndDir = playerStartDir.add(playerLookDir.scale(reach));
        playerLookDir.add(dirX, dirY, dirZ).normalize();
        //playerLookDir.add(0, 1000, 0).normalize();
        //SmallFireball fireball = new SmallFireball(level, dirX, dirY, dirZ, dir.normalize());;
        LargeFireball fireballAir = new LargeFireball(level, player, playerLookDir, explosionPowerAir);
        //Target entity
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                level, fireballAir, playerStartDir, playerEndDir, player.getBoundingBox()
                        .expandTowards(playerLookDir.scale(reach)).inflate(1.0),
                entity -> entity instanceof LivingEntity && entity != player);
        //Hit air or water
        if (blockHitResult.getType() != Type.BLOCK && entityHitResult == null) {
            //Fireball's initial spawn position
            Vec3 fireballInAirPosition = player.position().add(0, player.getEyeHeight() - 0.25, 0)
                    .add(playerLookDir.scale(2.5));
            //Spawns the fireball
            fireballAir.moveOrInterpolateTo(fireballInAirPosition);
            //Set's the fireball's velocity
            fireballAir.setDeltaMovement(playerLookDir.scale(velocity));
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            return fireballAir;
        //Hit entity
        } else if(entityHitResult != null) {
            Entity target = entityHitResult.getEntity();
            //Changes the fireball's position to the position of the entity we clicked on
            Vec3 fireballOnEntityPosition = target.position();
            //Teleports the fireball into the entity
            fireballAir.moveOrInterpolateTo(fireballOnEntityPosition);
            //Evil fake fireball explosion
            level.explode(fireballAir, fireballAir.getX(), fireballAir.getY(), fireballAir.getZ(),
                    explosionPowerEntity, Level.ExplosionInteraction.MOB);
            //Fireball is fake now, discards it when spawned so it doesn't appear after exploding
            fireballAir.discard();
            //Might add a sound effect later
            //level.playSound(null, dirX + 0.5, dirY, dirZ + 0.5, SoundEvents.PIG_DEATH, SoundSource.NEUTRAL, 1.0F, 1.0F);
            return fireballAir;
        } else {
            return null;
        }
    }
}

//TODO:
//Remove the fire shooting the projectile causes to improve performance
//Make it be able to destroy bedrock or any other block
//Make it so that it can spawn and shoot out other types of entities (maybe a different item)
//Make it so we can explode mountable entities
