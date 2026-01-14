package com.fireball_stick.initialization;

import com.fireball_stick.entity.ModEntities;
import com.fireball_stick.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTabs;

import static com.fireball_stick.tnt_stick.TNTStickClickBlock.tick;

public class ModInitialization implements ModInitializer {
    public static final String MOD_ID = "fireball_stick";

    public void onInitialize() {
        ModItems.init();

        //TNT STICK
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(ModItems.TNT_STICK));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(entries -> entries.accept(ModItems.TNT_STICK));

        //TNT STICK UNBOUND
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(ModItems.TNT_STICK_UNBOUND));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(entries -> entries.accept(ModItems.TNT_STICK_UNBOUND));

        //FIREBALL STICK
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(ModItems.FIREBALL_STICK));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
                .register(entries -> entries.accept(ModItems.FIREBALL_STICK));


        //CUSTOM TNT
        ModEntities.init();

        //Makes the tick-based placement of TNT work properly
        ServerTickEvents.END_SERVER_TICK.register(server -> tick());
    }
}
