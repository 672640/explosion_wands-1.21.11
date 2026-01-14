package com.fireball_stick.fireball_stick;

import com.fireball_stick.abstractClasses.BaseAsFireballProjectile;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FireballStickClickAir extends Item {
    public FireballStickClickAir(Properties properties) {
        super(properties);
    }

    //Initializes the item
    public static InteractionResult use(Item item, Level level, Player player, InteractionHand hand) {
        //ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (hitResult.getType() != HitResult.Type.BLOCK && !level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }

    public static Projectile asFireballProjectile(Item item, Level level, Player player, InteractionHand hand) {
        double min = 0.0;
        double max = 10.0;
        RandomSource random = RandomSource.create();
        double randomDistr1 = min + random.nextDouble() * (max - min);
        double randomDistr2 = min + random.nextDouble() * (max - min);
        double randomDistr3 = min + random.nextDouble() * (max - min);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        //Max distance we can click on an entity
        int reach = 1000;
        //Clicks on air/liquid
        int explosionPowerAir = 10;
        //Clicks on entity
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
        //Store the fireball's position
        Vec3 fireballPos = fireballAir.position();
        //Target entity
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                level, fireballAir, playerStartDir, playerEndDir, player.getBoundingBox()
                        .expandTowards(playerLookDir.scale(reach)).inflate(1.0),
                entity -> entity instanceof LivingEntity && entity != player);
        //Click on air/liquid
        if (blockHitResult.getType() != HitResult.Type.BLOCK && entityHitResult == null) {
            //Fireball's initial spawn position
            Vec3 fireballInAirPosition = player.position().add(0, player.getEyeHeight() - 0.25, 0)
                    .add(playerLookDir.scale(2.5));
            //Sets the fireball's position
            fireballAir.moveOrInterpolateTo(fireballInAirPosition);
            //Set's the fireball's velocity
            fireballAir.setDeltaMovement(playerLookDir.scale(velocity));
            //Plays sound when cliking on air/liquid
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            //Spawns the fireball
            return fireballAir;
            //Click on entity
        } else if(entityHitResult != null) {
            Entity target = entityHitResult.getEntity();
            //Changes the fireball's position to the position of the entity we clicked on
            Vec3 fireballOnEntityPosition = target.position();
            //Teleports the fireball into the entity
            fireballAir.moveOrInterpolateTo(fireballOnEntityPosition);
            //Evil fake fireball explosion
            level.explode(fireballAir, fireballAir.getX(), fireballAir.getY(), fireballAir.getZ(),
                    explosionPowerEntity, Level.ExplosionInteraction.MOB);
            if(level instanceof ServerLevel serverLevel) {
                //Particles spawn up to 32 blocks away from the player
                //32-bit integer limit: 2147483647
                serverLevel.sendParticles(new DustParticleOptions(16711680, 5), target.getX(), target.getY(), target.getZ(), 100, randomDistr1, randomDistr1, randomDistr1, 2);
                //serverLevel.sendParticles(new DustParticleOptions(10000000, 5), target.getX(), target.getY(), target.getZ(), 1000, 2, 2, 2, 2);
                serverLevel.sendParticles(new DustParticleOptions(500000, 5), target.getX(), target.getY(), target.getZ(), 100, randomDistr2, randomDistr2, randomDistr2, 2);
                serverLevel.sendParticles(new DustParticleOptions(3000, 5), target.getX(), target.getY(), target.getZ(), 100, randomDistr3, randomDistr3, randomDistr3, 2);
            }
            //Fireball is fake now, discards it when spawned so it doesn't appear after exploding
            fireballAir.discard();
            //Sound effect when clicking on entity
            level.playSound(null, dirX, dirY, dirZ, SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 0.1F, 1.0F);
            return fireballAir;
        } else {
            return null;
        }
    }
}
