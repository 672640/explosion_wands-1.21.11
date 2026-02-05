package com.explosion_wands.item_classes;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class TNTSlowBarrageWandItem extends Item {
    public TNTSlowBarrageWandItem(Properties properties) {
        super(properties);
    }

    //Click on block
    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        return com.explosion_wands.wands.TNTSlowBarrageWand.use(this, level, player, hand);
    }
}
