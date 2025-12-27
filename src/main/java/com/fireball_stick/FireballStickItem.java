package com.fireball_stick;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class FireballStickItem extends Item {
    public FireballStickItem(Item.Properties properties) {
        super(properties);
    }

    //Hit a block
    @Override
    public InteractionResult useOn(UseOnContext context) {
        return FireballStickClickBlock.useOn(this, context);
    }

    //Use animation of item
    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return FireballStickClickBlock.useAnimation(this, itemStack);
    }

    //How fast we can use the item
    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        return FireballStickClickBlock.useDuration(this, itemStack, user);
    }

    //Hit air, water or entity
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            Projectile projectile = FireballStickClickAir.asProjectile(this, level, player, hand);
            if (projectile != null) {
                level.addFreshEntity(projectile);
            }
        }
        return FireballStickClickAir.use(this, level, player, hand);
    }
}