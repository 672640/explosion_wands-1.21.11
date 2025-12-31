package com.fireball_stick.item;

import com.fireball_stick.FireballStickClickBlock;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.function.Function;

import static net.minecraft.core.Registry.register;
import static net.minecraft.world.item.Items.registerItem;

public class ModItems {
    public static final Item FIREBALL_STICK = registerItem("fireball_stick", Item::new);

    private static Item registerItem(String name, Function<Item.Settings, Item> function) {
        return Registry.register(Registries.ITEM, Identifier.of(FireballStickClickBlock.MOD_ID, name),
                function.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TutorialMod.MOD_ID, name)))));
    }

    public static void registerModItems() {
        FireballStickClickBlock.LOGGER.info("Registering Mod item for " + FireballStickClickBlock.MOD_ID);
    }
}
