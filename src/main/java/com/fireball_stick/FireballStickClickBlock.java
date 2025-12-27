package com.fireball_stick;

import net.fabricmc.api.ModInitializer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class FireballStickClickBlock implements ModInitializer {
	public static final String MOD_ID = "fireball_stick";

	@Override
	public void onInitialize() {
		Items.registerItem(modItemId("fireball_stick"), FireballStickItem::new, new Item.Properties());
	}

	private static ResourceKey<Item> modItemId(final String name) {
		return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name));
	}
	//Hits a block
	public static InteractionResult useOn(FireballStickItem FireballStickItem, UseOnContext context)  {

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
			level.playSound((Entity) null, clickedPos.getX() + 0.5, clickedPos.getY(), clickedPos.getZ() + 0.5, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0F, 1.0F);
			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.CONSUME;
		}
	}
	//Use animation of item
	public static ItemUseAnimation useAnimation(Item item, ItemStack itemStack) {
		Consumable consumable = (Consumable)itemStack.get(DataComponents.CONSUMABLE);
		return ItemUseAnimation.BLOCK;
	}
	//How fast we can use the item
	public static int useDuration(Item item, ItemStack itemStack, LivingEntity user) {
		//2 tick cooldown when block hit
		return 2;
	}
}
