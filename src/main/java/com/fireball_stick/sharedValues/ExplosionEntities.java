package com.fireball_stick.sharedValues;

import net.minecraft.util.RandomSource;

public class ExplosionEntities {
    private ExplosionEntities() {}
    public static int maxEntities = 1028;
    public static int fuse = 0;
    public static float minExplosion = 2F;
    public static float maxExplosion = 4F;
    //Amount of primedTNTs that spawn in the center
    public static int minIncrement = 2;
    public static int maxIncrement = 5;
    public static double lessThanTheta = Math.PI;
    public static double lessThanPhi = 2 * Math.PI;

    //Shared variables:
    public static double incrementPhi;
    //Decrease: more entities
    //Increase: less entities
    public static double incrementTheta = incrementPhi = 0.4;
    public static int increment = 0;
    public static double theta;
    public static double phi;
    public static double x;
    public static double y;
    public static double z = y = x = theta = phi = 0;


    public static double r = 2;
    public static int spawnHeight = 20;
    public static int reach = 128;
    public static int spawnedEntities = (int) ((Math.floor(lessThanTheta / incrementTheta) + 1) * (Math.floor(lessThanPhi / incrementPhi) + 1));
    public static int minRandomEntity = 1;
    public static int maxRandomEntity = spawnedEntities;

}
