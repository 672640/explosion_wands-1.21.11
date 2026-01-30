package com.fireball_stick.initialization;

import com.fireball_stick.entity.ModEntities;
import com.fireball_stick.item.ModItems;
import com.fireball_stick.item_classes.air.TNTStickMidAirItem;
import com.fireball_stick.tick.TickQueueManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;

public class ModInitialization implements ModInitializer {
    public static final String MOD_ID = "fireball_stick";

    public void onInitialize() {
        ModItems.init();
        //CUSTOM TNT
        ModEntities.init();

//AIR

        //TNT STICK AIR
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(ModItems.TNT_STICK_AIR));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(entries -> entries.accept(ModItems.TNT_STICK_AIR));

        //TNT STICK UNBOUND AIR
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(ModItems.TNT_STICK_UNBOUND_AIR));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(entries -> entries.accept(ModItems.TNT_STICK_UNBOUND_AIR));

        //TNT STICK SHOTGUN AIR
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(ModItems.FIREBALL_STICK_SHOTGUN_AIR));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(entries -> entries.accept(ModItems.FIREBALL_STICK_SHOTGUN_AIR));

        //TNT STICK MID AIR

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(ModItems.TNT_STICK_MID_AIR));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(entries -> entries.accept(ModItems.TNT_STICK_MID_AIR));

        //FIREBALL STICK AIR
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(ModItems.FIREBALL_STICK_AIR));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(entries -> entries.accept(ModItems.FIREBALL_STICK_AIR));

        //FIREBALL STICK HITSCAN AIR

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(ModItems.FIREBALL_STICK_HITSCAN_AIR));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(entries -> entries.accept(ModItems.FIREBALL_STICK_HITSCAN_AIR));

//BLOCK

        //FIREBALL STICK BLOCK
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(ModItems.FIREBALL_STICK_BLOCK));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(entries -> entries.accept(ModItems.FIREBALL_STICK_BLOCK));

        //TNT STICK UNBOUND BLOCK
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(ModItems.TNT_STICK_BLOCK));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(entries -> entries.accept(ModItems.TNT_STICK_BLOCK));

        //TNT STICK FALLING BLOCK
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(ModItems.TNT_STICK_FALLING_BLOCK));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(entries -> entries.accept(ModItems.TNT_STICK_FALLING_BLOCK));
        //TNT STICK ENTITIES BLOCK
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(ModItems.TNT_STICK_ENTITIES_BLOCK));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(entries -> entries.accept(ModItems.TNT_STICK_ENTITIES_BLOCK));
        //FIREBALL STICK BLOCK
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(ModItems.TNT_STICK_UNBOUND_BLOCK));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(entries -> entries.accept(ModItems.TNT_STICK_UNBOUND_BLOCK));

        //TNT FIREBALL STICK EXPLOSION BLOCK
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(ModItems.TNT_FIREBALL_STICK_EXPLOSION_BLOCK));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(entries -> entries.accept(ModItems.TNT_FIREBALL_STICK_EXPLOSION_BLOCK));

        //Makes the tick-based placement of TNT work properly
        ServerTickEvents.END_SERVER_TICK.register(server -> TickQueueManager.tick());
    }
}
