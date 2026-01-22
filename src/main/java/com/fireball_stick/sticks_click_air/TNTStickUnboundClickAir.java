package com.fireball_stick.sticks_click_air;

import com.fireball_stick.customFunctions.tnt.CustomTnt;
import com.fireball_stick.entity.ModEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class TNTStickUnboundClickAir extends Item {
    public TNTStickUnboundClickAir(Properties properties) {
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

    public static PrimedTnt asPrimedTnt(Item item, Level level, Player player, InteractionHand hand) {
        int velocity = 4;
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        double dirX = player.getX();
        double dirY = player.getY();
        double dirZ = player.getZ();

        Vec3 playerLookDir = player.getLookAngle();
        playerLookDir.add(dirX, dirY, dirZ).normalize();
        CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
        if(customTnt != null) {
            if (blockHitResult.getType() == HitResult.Type.BLOCK) {
                Vec3 customTntInAirPosition = player.position().add(0, player.getEyeHeight() - 0.25, 0)
                        .add(playerLookDir.scale(3.0));
                customTnt.moveOrInterpolateTo(customTntInAirPosition);
                } else {
                //Works for the most part
                Vec3 customTntInAirPosition = blockHitResult.getLocation();
                customTnt.moveOrInterpolateTo(customTntInAirPosition);
                }
                customTnt.setDeltaMovement(playerLookDir.scale(velocity));
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.TNT_PRIMED, SoundSource.PLAYERS, 0.4F, 1.0F);
                customTnt.setDiscardOnFirstUse(false);
                customTnt.setExplodeOnContact(true);
                //ExplosionPower >= 24.0F means that it can explode blocks in water
                //Lazy workaround for the primedTNT exploding every tick if it's in water when
                //setDiscardOnFirstUse(false) and setExplodeOnContact(true).
                customTnt.setExplosionPower(4F);
                customTnt.setFuse(150);
                return customTnt;
            }
        return null;
}
}
//TODO: change explosion effects to be larger maybe
//Different explosion effects (f.ex. flame particles, like Mad Maggie's ult)
//Effect after getFuse = 0