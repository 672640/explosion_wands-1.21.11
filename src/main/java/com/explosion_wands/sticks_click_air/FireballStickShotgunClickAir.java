package com.explosion_wands.sticks_click_air;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FireballStickShotgunClickAir extends Item {
    public FireballStickShotgunClickAir(Properties properties) {
        super(properties);
    }

    //Initializes the item
    public static InteractionResult use(Item item, Level level, Player player, InteractionHand hand) {
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (hitResult.getType() != HitResult.Type.BLOCK && !level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }

    public static Projectile asFireballProjectile(Item item, Level level, Player player, InteractionHand hand) {
        if(level instanceof ServerLevel server) {
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        //Max distance we can click on an entity
        int reach = 360;
        double incremented = 2;
        double changePos = 0;
        double fireballAmount = 50;
        //Clicks on air/liquid
        int explosionPowerAir = 9;
        //fireball's velocity
        double velocity = 3;
        double dirX = player.getX();
        double dirY = player.getY();
        double dirZ = player.getZ();
        //Looking directly up
            double directlyUpDir = Math.toRadians(player.getXRot() + 90);
            //Looking directly down
            double directlyDownDir = Math.toRadians(player.getXRot() - 90);
            /*
            if(directlyUpDir == 0) {
                System.out.println("Directly up!");
            }
            if(directlyDownDir == 0) {
                System.out.println("Directly down!");
            }
             */
        //Vec3 dir = new Vec3(dirX, dirY, dirZ);
        Vec3 playerStartDirForward = player.getLookAngle().normalize();
        Vec3 directlyUp = new Vec3(0,1,0);
        if(directlyUpDir == 0 || directlyDownDir == 0) {
            directlyUp = new Vec3(0,0,1);
        }
        Vec3 playerStartDir = player.getEyePosition();
        Vec3 playerEndDir = playerStartDir.add(playerStartDirForward.scale(reach));
        playerStartDirForward.add(dirX, dirY, dirZ).normalize();
            final double playerLookAngle = Math.toRadians(player.getXRot());
            //Sets the x and z directions to these numbers as a workaround for when we look directly up or down, which makes
            //the vectors be (0,0,0) no matter what. This should be indistinguishable compared to if they are 0
            Vec3 playerStartDirRight = playerStartDirForward.cross(directlyUp).normalize();
        Vec3 playerStartDirLeft = playerStartDirRight.cross(playerStartDirForward).normalize();
        float yaw = player.getYRot();
        double rad = Math.toRadians(yaw);
        //playerLookDir.add(0, 1000, 0).normalize();
        for(int i = 0; i < fireballAmount; i++) {
            LargeFireball fireballAir = new LargeFireball(level, player, playerStartDirForward, explosionPowerAir);
            //Fireball's initial spawn position
            if(blockHitResult.getType() != HitResult.Type.BLOCK) {
                    Vec3 fireballInAirPosition = playerStartDir.add(playerStartDirForward.scale(2.5)) //in front
                            //Ensures that the fireballs are evenly distributed in front of the player
                            .add(playerStartDirRight.scale((incremented * (fireballAmount / 2)) - (changePos + incremented / 2))) //left/right
                            .add(directlyUp.scale(-0.25)); //up/down
                    //Sets the fireball's position
                    fireballAir.moveOrInterpolateTo(fireballInAirPosition);
            }
            if(blockHitResult.getType() == HitResult.Type.BLOCK) {
                    Vec3 fireballInAirPosition = blockHitResult.getLocation() //in front
                            //Ensures that the fireballs are evenly distributed in front of the player
                            .add(playerStartDirRight.scale((incremented * (fireballAmount / 2)) - (changePos + incremented / 2))) //left/right
                            .add(0, +0.25, 0); //up/down
                    //Sets the fireball's position
                    fireballAir.moveOrInterpolateTo(fireballInAirPosition);
            }
            //Set's the fireball's velocity
            fireballAir.setDeltaMovement(playerStartDirForward.scale(velocity));
            //Ensures the sound is not played for every single fireball that spawns
            fireballAir.addTag("fireball");
            //Spawns the fireball
            server.addFreshEntity(fireballAir);
            if(fireballAir.touchingUnloadedChunk()) {
                fireballAir.discard();
            }
            changePos += incremented;
        }
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 0.8F, 1.0F);
        }
        return null;
    }
 }

 //TODO: When we look directly up/down, the fireballs' positions are the exact same. Ensure they are
//spread out the same way as when we look any other direction - band aid-fixed
