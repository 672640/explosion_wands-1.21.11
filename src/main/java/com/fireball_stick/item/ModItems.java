package com.fireball_stick.item;

import com.fireball_stick.item_classes.air.*;
import com.fireball_stick.initialization.ModInitialization;
import com.fireball_stick.item_classes.block.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItems {
//AIR

    //TNT STICK AIR
    public static final ResourceKey<Item> TNT_STICK_AIR_KEY =
            key("tnt_stick_air");

    public static final Item TNT_STICK_AIR =
            register(TNT_STICK_AIR_KEY,
                    new TNTStickAirItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .setId(TNT_STICK_AIR_KEY)));

    //TNT STICK UNBOUND AIR
    public static final ResourceKey<Item> TNT_STICK_UNBOUND_AIR_KEY =
            key("tnt_stick_unbound_air");

    public static final Item TNT_STICK_UNBOUND_AIR =
            register(TNT_STICK_UNBOUND_AIR_KEY,
                    new TNTStickUnboundAirItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .setId(TNT_STICK_UNBOUND_AIR_KEY)));

    //TNT STICK MID AIR
    public static final ResourceKey<Item> TNT_STICK_MID_AIR_KEY =
            key("tnt_stick_mid_air");

    public static final Item TNT_STICK_MID_AIR =
            register(TNT_STICK_MID_AIR_KEY,
                    new TNTStickMidAirItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .setId(TNT_STICK_MID_AIR_KEY)));

    //FIREBALL STICK AIR
    public static final ResourceKey<Item> FIREBALL_STICK_AIR_KEY =
            key("fireball_stick_air");

    public static final Item FIREBALL_STICK_AIR =
            register(FIREBALL_STICK_AIR_KEY,
                    new FireballStickAirItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .setId(FIREBALL_STICK_AIR_KEY)));

    //FIREBALL STICK SHOTGUN AIR
    public static final ResourceKey<Item> FIREBALL_STICK_SHOTGUN_AIR_KEY =
            key("fireball_stick_shotgun_air");

    public static final Item FIREBALL_STICK_SHOTGUN_AIR =
            register(FIREBALL_STICK_SHOTGUN_AIR_KEY,
                    new FireballStickShotgunAirItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .setId(FIREBALL_STICK_SHOTGUN_AIR_KEY)));

    //FIREBALL STICK HITSCAN AIR
    public static final ResourceKey<Item> FIREBALL_STICK_HITSCAN_AIR_KEY =
            key("fireball_stick_hitscan_air");

    public static final Item FIREBALL_STICK_HITSCAN_AIR =
            register(FIREBALL_STICK_HITSCAN_AIR_KEY,
                    new FireballStickHitscanAirItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .setId(FIREBALL_STICK_HITSCAN_AIR_KEY)));
//BLOCK

    //FIREBALL STICK BLOCK
    public static final ResourceKey<Item> FIREBALL_STICK_BLOCK_KEY =
            key("fireball_stick_block");

    public static final Item FIREBALL_STICK_BLOCK =
            register(FIREBALL_STICK_BLOCK_KEY,
                    new FireballStickBlockItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .setId(FIREBALL_STICK_BLOCK_KEY)));


    //TNT STICK BLOCK
    public static final ResourceKey<Item> TNT_STICK_BLOCK_KEY =
            key("tnt_stick_block");

    public static final Item TNT_STICK_BLOCK =
            register(TNT_STICK_BLOCK_KEY,
                    new TNTStickBlockItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .setId(TNT_STICK_BLOCK_KEY)));

    //TNT STICK UNBOUND BLOCK
    public static final ResourceKey<Item> TNT_STICK_UNBOUND_BLOCK_KEY =
            key("tnt_stick_unbound_block");

    public static final Item TNT_STICK_UNBOUND_BLOCK =
            register(TNT_STICK_UNBOUND_BLOCK_KEY,
                    new TNTStickUnboundBlockItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .setId(TNT_STICK_UNBOUND_BLOCK_KEY)));

    //TNT STICK FALLING BLOCK
    public static final ResourceKey<Item> TNT_STICK_FALLING_BLOCK_KEY =
            key("tnt_stick_falling_block");

    public static final Item TNT_STICK_FALLING_BLOCK =
            register(TNT_STICK_FALLING_BLOCK_KEY,
                    new TNTStickFallingBlockItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .setId(TNT_STICK_FALLING_BLOCK_KEY)));
    //TNT STICK ENTITIES BLOCK
    public static final ResourceKey<Item> TNT_STICK_ENTITIES_BLOCK_KEY =
            key("tnt_stick_entities_block");

    public static final Item TNT_STICK_ENTITIES_BLOCK =
            register(TNT_STICK_ENTITIES_BLOCK_KEY,
                    new TNTStickEntitiesBlockItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .setId(TNT_STICK_ENTITIES_BLOCK_KEY)));
    //TNT FIREBALL STICK EXPLOSION BLOCK
    public static final ResourceKey<Item> TNT_FIREBALL_STICK_EXPLOSION_BLOCK_KEY =
            key("tnt_fireball_stick_explosion_block");

    public static final Item TNT_FIREBALL_STICK_EXPLOSION_BLOCK =
            register(TNT_FIREBALL_STICK_EXPLOSION_BLOCK_KEY,
                    new TNTFireballStickExplosionBlockItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .setId(TNT_FIREBALL_STICK_EXPLOSION_BLOCK_KEY)));
//HELPER METHODS
    //Creating the item's identity
    private static ResourceKey<Item> key(String name) {
        return ResourceKey.create(
                Registries.ITEM,
                Identifier.fromNamespaceAndPath(ModInitialization.MOD_ID, name));
    }

    //Registering the item
    private static Item register(ResourceKey<Item> key, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    //Initializes the item
    public static void init() {}

}
