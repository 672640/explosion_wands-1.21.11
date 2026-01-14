package com.fireball_stick.item;

import com.fireball_stick.fireball_stick.FireballStickItem;
import com.fireball_stick.initialization.ModInitialization;
import com.fireball_stick.tnt_stick_unbound.TNTStickUnboundItem;
import com.fireball_stick.tnt_stick.TNTStickItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.Item;

public class ModItems {
//TNT STICK

    public static final ResourceKey<Item> TNT_STICK_KEY =
            key("tnt_stick");

    public static final Item TNT_STICK =
            register(TNT_STICK_KEY,
                    new TNTStickItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .setId(TNT_STICK_KEY)));
    //TNT STICK UNBOUND
    public static final ResourceKey<Item> TNT_STICK_UNBOUND_KEY =
            key("tnt_stick_unbound");

    public static final Item TNT_STICK_UNBOUND =
            register(TNT_STICK_UNBOUND_KEY,
                    new TNTStickUnboundItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .setId(TNT_STICK_UNBOUND_KEY)));

    //FIREBALL STICK
    public static final ResourceKey<Item> FIREBALL_STICK_KEY =
            key("fireball_stick");

    public static final Item FIREBALL_STICK =
            register(FIREBALL_STICK_KEY,
                new FireballStickItem(
                        new Item.Properties()
                            .stacksTo(1)
                            .setId(FIREBALL_STICK_KEY)));

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
