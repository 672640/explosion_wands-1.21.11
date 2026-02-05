package com.explosion_wands.item_classes;

import com.explosion_wands.wands.TNTFallingWand;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class TNTFallingWandItem extends Item {
    public TNTFallingWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        return TNTFallingWand.use(this, level, player, hand);
    }
}
