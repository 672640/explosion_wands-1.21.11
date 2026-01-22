package com.fireball_stick.sticks_click_block;

import com.fireball_stick.customFunctions.tnt.CustomTnt;
import com.fireball_stick.entity.ModEntities;
import com.fireball_stick.tick.TickQueue;
import com.fireball_stick.tick.TickQueueManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class TNTStickClickBlock {
	static int tntAmountPerTick = 4;
	private static final int tntAmount = 100;
	static int iterations = 0;
	private static List<Runnable> QUEUE = new ArrayList<>();
	private static int tickCounter = 0;
    private static final Queue<Runnable> nextTickQueue = new ArrayDeque<>();
	//Queue<Runnable> queue = new ArrayDeque<>();
	public static void add(Runnable task) {
		QUEUE.add(task);
	}
/*
	//Old tick queue system. Works for 1 queue at a time by clearing any other queues that are triggered before this one is finished
	public static void tick() {
		//Ensures that block hits that are happening before the previous queue(s) have finished are not queued for later;
		//Only allows one complete primed TNT iteration to happen at a time. Might expand to make them run in parallel in the future
		if (QUEUE.isEmpty() || iterations > tntAmount) {
			iterations = 0;
			QUEUE.clear();
			return;
		}
		iterations+= tntAmountPerTick;
		tickCounter++;
		if (tickCounter >= 1 && iterations <= tntAmount) {
				//Resets the tick counter
				tickCounter = 0;
				//Speeds up the iteration of placing the primed TNTs
            for(int i = 0; i < tntAmountPerTick; i++) {
					QUEUE.remove(0).run();
				}
				//System.out.println("Iterations: " + iterations);
		}
	}
 */


	//Hits a block
	public static InteractionResult use(Item item, Level level, Player player, InteractionHand hand)  {
		/*
		BlockPlaceContext placeContext = new BlockPlaceContext(context);
		BlockPos clickedPos = placeContext.getClickedPos();
		Level level = context.getLevel();
		Player player = context.getPlayer();
		 */

		if (level instanceof ServerLevel serverLevel && player != null && !level.isClientSide()) {
			TickQueue queue = TickQueueManager.createQueue(tntAmount, 4);
			int reach = 1000;
			final double[] spawnHeight = {30};
			/*
			double xDir = clickedPos.getX();
			double yDir = clickedPos.getY();
			double zDir = clickedPos.getZ();
			 */
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
			int tntFuseTimer = (tntAmount * 50) / (50 * tntAmountPerTick) ; //50 ms = 1 tick
			Vec3 playerEyeStart = player.getEyePosition();
			Vec3 playerLookAngle = player.getLookAngle();
			Vec3 playerEyeEnd = playerEyeStart.add(playerLookAngle.scale(reach));
			BlockHitResult blockHitResult = level.clip(new ClipContext(
					playerEyeStart,
					playerEyeEnd,
					ClipContext.Block.COLLIDER,
					ClipContext.Fluid.NONE,
					player
			));
			BlockPos target = blockHitResult.getBlockPos();
			final double[] changePosition = {0}; //Initial position of the starting TNT
				for (int i = 0; i < tntAmount; i++) {
						//Fires a TNT at the interval specified in tick()
					int finalI = i;
					//Adds one primed TNT based on the tickCounter
					int finalI1 = i;
					queue.add(() -> {
						//Creates primed TNTs every iteration
						CustomTnt customTnt = ModEntities.CUSTOM_TNT.create(level, EntitySpawnReason.TRIGGERED);
						if(customTnt != null) {
							//X dir: cos, Z dir: sin, makes a circle
							customTnt.setPos(target.getX() + (Math.cos(angle[0]) * amplitude),
									target.getY() + spawnHeight[0],
									target.getZ() + (Math.sin(angle[0]) * amplitude));
							customTnt.setFuse(tntFuseTimer);
							//Performance improvement: Spawns a particle effect on each TNT that satisfy the modulus criteria instead of on each TNT
							if ((finalI % 6) == 1) {
								//Particles only spawn 32 blocks away from the player. Might bypass in future
								serverLevel.sendParticles(ParticleTypes.COPPER_FIRE_FLAME, customTnt.getX(), customTnt.getY(), customTnt.getZ(), 700, randomDistr, randomDistr, randomDistr, 1);
							}
							customTnt.setExplosionPower(4.0F);
							customTnt.setExplodeOnContact(false);
							customTnt.setDefaultGravity(0.15);
							//Changes the initial angle by the value of angleStep every iteration so the TNTs are not static
							angle[0] += angleStep;
							//Height of the cos curve every iteration
							changePosition[0] += Math.PI / ((double) (tntAmount / 4) / 2);
							spawnHeight[0] -= 0.25;
							//Adds the primed TNT to the world
							serverLevel.addFreshEntity(customTnt);
							//Kind of a hacky way to play a sound only at the very start of the loop
							if(finalI1 == 0) {
								level.playSound(null,
										blockHitResult.getBlockPos().getX(),
										//Makes the sound play as close to the y direction the player is at
										blockHitResult.getBlockPos().getY() + spawnHeight[0],
										blockHitResult.getBlockPos().getZ(),
										SoundEvents.TNT_PRIMED,
										SoundSource.PLAYERS,
										0.6F, 1.0F);
							}
						}
					});
                }
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
