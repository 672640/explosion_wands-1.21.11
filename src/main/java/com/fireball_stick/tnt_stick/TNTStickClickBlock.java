package com.fireball_stick.tnt_stick;

import com.fireball_stick.customFunctions.CustomTnt;
import com.fireball_stick.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TNTStickClickBlock {
	private static final List<Runnable> QUEUE = new ArrayList<>();
	private static int tickCounter = 0;
	private static int taskCount = 0;
	private static final Queue<Runnable> nextTickQueue = new ArrayDeque<>();
	public static void add(Runnable task) {
		QUEUE.add(task);
	}

	//Tick queue system
	public static void tick() {
		if (QUEUE.isEmpty()) {
			return;
		}
		tickCounter++;
		if (tickCounter >= 1) {
			//Resets the tick counter
			tickCounter = 0;
			QUEUE.remove(0).run();
		}
	}

	//Hits a block
	public static InteractionResult useOn(UseOnContext context)  {
		BlockPlaceContext placeContext = new BlockPlaceContext(context);
		BlockPos clickedPos = placeContext.getClickedPos();
		Level level = context.getLevel();
		Player player = context.getPlayer();

		if (level instanceof ServerLevel serverLevel && player != null && !level.isClientSide()) {
			float explosionPower = 5F;
			double defaultGravity = 0.04;
			boolean explodeOnContact = false;
			double xDir = clickedPos.getX();
			double yDir = clickedPos.getY();
			double zDir = clickedPos.getZ();
			int tntAmount = 100;
			double min = 1.0;
			double max = 4.0;
			RandomSource random = RandomSource.create();
			//Randomized the distribution of particle effects based on the min/max values specified
			double randomDistr = min + random.nextDouble() * (max - min);
			//Makes the start spawn angle of the TNT be equal to the direction the player is facing (default (0): east)
			final double[] angle = {Math.toRadians(player.getYRot() + 90)};
			double angleStep = Math.PI / ((double) tntAmount / 2); //How smooth the curve looks
			double amplitude = 15; //Width of the curve
			//Making sure the primed TNTs explode when all the primed TNTs in the current loop has spawned
			int tntFuseTimer = (tntAmount * 50) / 50 ; //50 ms = 1 tick
			final double[] changePosition = {0}; //Initial position of the starting TNT
				for (int i = 0; i < tntAmount; i++) {
						//Fires a TNT at the interval specified in tick()
					int finalI = i;
					//Adds one primed TNT based on the tickCounter
					add(() -> {
						//Creates primed TNTs every iteration
						CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
						//X dir: cos, Z dir: sin, makes a circle
						customTnt.setPos(xDir + (Math.cos(angle[0]) * amplitude),
								yDir + 3 + Math.cos(changePosition[0]) * 5,
								zDir + (Math.sin(angle[0]) * amplitude));
						customTnt.setFuse(tntFuseTimer);
						//Adds the primed TNT to the world
						serverLevel.addFreshEntity(customTnt);
						//Performance improvement: Spawns a particle effect on each TNT that satisfy the modulus criteria instead of on each TNT
						if((finalI % 6) == 1) {
							//Particles only spawn 32 blocks away from the player. Might bypass in future
							serverLevel.sendParticles(ParticleTypes.COPPER_FIRE_FLAME, customTnt.getX(), customTnt.getY(), customTnt.getZ(), 700, randomDistr, randomDistr, randomDistr, 1);
						}
						customTnt.setExplosionPower(explosionPower);
						customTnt.setExplodeOnContact(explodeOnContact);
						//Changes the initial angle by the value of angleStep every iteration so the TNTs are not static
						angle[0] += angleStep;
						//Height of the cos curve every iteration
						changePosition[0] += Math.PI / ((double) (tntAmount / 4) / 2);
					});
                }
			//Plays a sound when a block is clicked
			level.playSound(null, clickedPos.getX(), clickedPos.getY(), clickedPos.getZ(), SoundEvents.TNT_PRIMED, SoundSource.PLAYERS, 0.1F, 1.0F);
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
		return 200;
	}
}
