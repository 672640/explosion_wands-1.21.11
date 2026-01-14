package com.fireball_stick.abstractClasses;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class BaseAsFireballProjectile extends Item {
    //Currently unused, will most likely be used when implementing functionality for both tnt_stick versions
    public BaseAsFireballProjectile(Properties properties) {
        super(properties);
    }

    protected abstract void cast(Item item, Level level, Player player, InteractionHand hand);
}

//TODO:
//Remove the fire shooting the projectile causes to improve performance
//Make it be able to destroy bedrock or any other block
//Make it so that it can spawn and shoot out other types of entities (maybe a different item)
//Make it so we can explode mountable entities
