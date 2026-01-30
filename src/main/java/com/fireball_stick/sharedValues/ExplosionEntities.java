package com.fireball_stick.sharedValues;

import net.minecraft.util.RandomSource;

public class ExplosionEntities {
    private ExplosionEntities() {}
    public static int maxEntities = 1028;
    public static int fuse = 0;
    public static float minExplosion = 1F;
    public static float maxExplosion = 4F;
    public static int minIncrement = 0;
    public static int maxIncrement = 3;
    public static int explosionKnockback = 1;
    public static double lessThanTheta = Math.PI;
    public static double lessThanPhi = 2 * Math.PI;

    //Shared variables:
    public static double incrementPhi;
    //Decrease: more entities
    //Increase: less entities
    public static double incrementTheta = incrementPhi = 0.4;
    public static int increment;
    public static double theta;
    public static double phi;
    public static double x;
    public static double y;
    public static double z = y = x = theta = phi = increment = 0;


    public static double r = 1;
    public static int spawnHeight = 20;
    public static int reach = 1000;
    public static int spawnedEntities = (int) ((Math.floor(lessThanTheta / incrementTheta) + 1) * (Math.floor(lessThanPhi / incrementPhi) + 1));
    public static int minRandomEntity = 1;
    public static int maxRandomEntity = spawnedEntities;
    //public static int spawnedEntitiesComparisonAmount = 4;
    //public static int spawnedEntitiesComparison = (int) Math.pow(2, spawnedEntitiesComparisonAmount - 1);

}
