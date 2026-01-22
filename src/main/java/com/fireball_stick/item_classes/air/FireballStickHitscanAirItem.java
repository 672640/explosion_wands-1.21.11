package com.fireball_stick.item_classes.air;

import com.fireball_stick.sticks_click_air.FireballStickHitscanClickAir;
import com.fireball_stick.sticks_click_air.FireballStickShotgunClickAir;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

public class FireballStickHitscanAirItem extends Item {
    public FireballStickHitscanAirItem(Properties properties) {
        super(properties);
    }

    //Click on air/liquid/entity
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            Projectile projectile = FireballStickHitscanClickAir.asFireballProjectile(this, level, player, hand);
            if (projectile != null) {
                level.addFreshEntity(projectile);
            }
        }
        return FireballStickHitscanClickAir.use(this, level, player, hand);
    }
}
