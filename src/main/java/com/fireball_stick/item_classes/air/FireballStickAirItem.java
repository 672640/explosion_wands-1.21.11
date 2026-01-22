package com.fireball_stick.item_classes.air;

import com.fireball_stick.sticks_click_air.FireballStickClickAir;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class FireballStickAirItem extends Item {
    public FireballStickAirItem(Properties properties) {
        super(properties);
    }

    //Click on air/liquid/entity
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            Projectile projectile = FireballStickClickAir.asFireballProjectile(this, level, player, hand);
            level.addFreshEntity(projectile);
        }
        return FireballStickClickAir.use(this, level, player, hand);
    }
}
