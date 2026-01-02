package com.fireball_stick;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
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
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static net.minecraft.world.entity.EntityType.TNT;
import static net.minecraft.world.item.Items.registerItem;
public class FireballStickClickBlock implements ModInitializer {
	public static final String MOD_ID = "fireball_stick";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		registerItem(modItemId("fireball_stick"), FireballStickItem::new, new Item.Properties());
	}

	private static ResourceKey<Item> modItemId(final String name) {
		return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name));
	}

	//Hits a block
	public static InteractionResult useOn(FireballStickItem FireballStickItem, UseOnContext context)  {
		//Default TNT explode timer: 80 ticks
		int timeBetweenEachTntPlacement = 30; //milliseconds
		int tntAmount = 50;
		//50 ms = 1 tick
		int tntFuseTimer = (tntAmount * timeBetweenEachTntPlacement) / 50 ; //ticks
		//360 degrees or 2 PI = full circle
		BlockPlaceContext placeContext = new BlockPlaceContext(context);
		BlockPos clickedPos = placeContext.getClickedPos();
		Level level = context.getLevel();
		Player player = context.getPlayer();
		//FireChargeItem fireChargeItem = new FireChargeItem(new Item.Properties());
		if (level instanceof ServerLevel serverLevel &&
				serverLevel.getBlockState(clickedPos).canBeReplaced() && player != null) {
			//serverLevel.explode(primedTnt, clickedPos.getX(), clickedPos.getY(), clickedPos.getZ(),
					//explosionPowerBlock, ServerLevel.ExplosionInteraction.BLOCK);
			if(!level.isClientSide()) {
				//full circle = 2 PI
				double halfCircleSine = Math.toRadians(2 * Math.PI / ((double) tntAmount / 2));
				double halfCircleCos = Math.toRadians(2 * Math.PI / ((double) tntAmount / 2));
				int i;
				double changePosition = 1;
				for (i = 0; i < tntAmount; i++) {
					Vec3 playerLookDir = player.getLookAngle();
					double xDir = clickedPos.getX();
					double yDir = clickedPos.getY();
					double zDir = clickedPos.getZ();
					PrimedTnt primedTnt = new PrimedTnt(level,
							xDir + halfCircleSine + halfCircleCos,
							yDir,
							zDir + changePosition,
							player);
						primedTnt.setFuse(tntFuseTimer);
						serverLevel.addFreshEntity(primedTnt);
						changePosition = changePosition + 3;
						if(halfCircleSine <= Math.PI && halfCircleSine >= 0) {
							halfCircleSine = halfCircleSine + tntAmount - (tntAmount + i);
							halfCircleCos = 0;
						}
						else if(halfCircleSine > Math.PI && halfCircleSine < 2 * Math.PI) {
							halfCircleCos = halfCircleSine + tntAmount - (tntAmount + i);
							halfCircleSine = 0;
						}
					try {
						TimeUnit.MILLISECONDS.sleep(timeBetweenEachTntPlacement);
					} catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
			}
			//Plays a sound when placed
			//level.playSound((Entity) null, clickedPos.getX(), clickedPos.getY(), clickedPos.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0F, 1.0F);
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
		//cooldown for next block hit
		return 20;
	}
}
