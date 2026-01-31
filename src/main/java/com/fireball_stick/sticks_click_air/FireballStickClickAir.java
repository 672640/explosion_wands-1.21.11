package com.fireball_stick.sticks_click_air;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class FireballStickClickAir extends Item {
    public FireballStickClickAir(Properties properties) {
        super(properties);
    }

    //Initializes the item
    public static InteractionResult use(Item item, Level level, Player player, InteractionHand hand) {
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
        int explosionPowerAir = 60;
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
        //Fireball's initial spawn position
        if(blockHitResult.getType() != HitResult.Type.BLOCK) {
            Vec3 fireballInAirPosition = player.position().add(0, player.getEyeHeight() - 0.25, 0)
                    .add(playerLookDir.scale(2.5));
            //Sets the fireball's position
            fireballAir.moveOrInterpolateTo(fireballInAirPosition);
        } else {
            //Does not work if it's at the very corner of a block, but it's more than good enough
            Vec3 fireballInAirPosition = blockHitResult.getLocation();
            //Sets the fireball's position
            fireballAir.moveOrInterpolateTo(fireballInAirPosition);
        }
        //Set's the fireball's velocity
        fireballAir.setDeltaMovement(playerLookDir.scale(velocity));
        //Plays sound when cliking on air/liquid
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 0.4F, 1.0F);
        //Spawns the fireball
        return fireballAir;
    }
}
