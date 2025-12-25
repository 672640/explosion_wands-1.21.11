package com.example.modstick;

import net.fabricmc.api.ModInitializer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.gamerules.GameRules;

public class ModStickMain implements ModInitializer {
	public static final String MOD_ID = "modstick";

	@Override
	public void onInitialize() {
		Items.registerItem(modItemId("mod_stick"), ModStickItem::new, new Item.Properties());
	}

	private static ResourceKey<Item> modItemId(final String name) {
		return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name));
	}

	public static InteractionResult useOn(ModStickItem modStickItem, UseOnContext context)  {

		BlockPlaceContext placeContext = new BlockPlaceContext(context);

		BlockPos clickedPos = placeContext.getClickedPos();

		Level level = context.getLevel();

		Player player = context.getPlayer();

		PrimedTnt primedTnt = new PrimedTnt(level, clickedPos.getX() + 0.5, clickedPos.getY(), clickedPos.getZ() + 0.5, player);

		//FireChargeItem fireChargeItem = new FireChargeItem(new Item.Properties());
		if (level instanceof ServerLevel serverLevel && serverLevel.getBlockState(clickedPos).canBeReplaced() && player != null) {
			//TNT explodes after 0 ticks (instantly)
			primedTnt.setFuse(0);
			//Adds the primed TNT to the world
			serverLevel.addFreshEntity(primedTnt);
			//Player animation of using the item
			player.startUsingItem(context.getHand());
			//Plays a sound when placed
			level.playSound((Entity) null, clickedPos.getX() + 0.5, clickedPos.getY(), clickedPos.getZ() + 0.5, SoundEvents.VILLAGER_DEATH, SoundSource.NEUTRAL, 1.0F, 1.0F);

			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.CONSUME;
		}
	}

	public static ItemUseAnimation useAnimation(Item item, ItemStack itemStack) {
		Consumable consumable = (Consumable)itemStack.get(DataComponents.CONSUMABLE);
		return ItemUseAnimation.BLOCK;
	}

	public static int useDuration(Item item, ItemStack itemStack, LivingEntity user) {
		//2 tick cooldown per usage (20 ticks per second)
		return 2;
	}
}
