package com.explosion_wands.entity;

import com.explosion_wands.customFunctions.falling_block.CustomFallingBlockEntity;
import com.explosion_wands.customFunctions.tnt.CustomTnt;
import com.explosion_wands.initialization.ModInitialization;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.*;

public class ModEntities {

    //CUSTOM TNT
    public static final ResourceKey<EntityType<?>> CUSTOM_TNT_KEY =
            key("custom_tnt");

    public static final EntityType<CustomTnt> CUSTOM_TNT =
            register(CUSTOM_TNT_KEY,
                            EntityType
                                    .Builder
                                    .of(CustomTnt::new, MobCategory.MISC)
                                    .sized(0.98F, 0.98F)
                                    .build(CUSTOM_TNT_KEY));

    //CUSTOM FALLING BLOCK ENTITY
    public static final ResourceKey<EntityType<?>> CUSTOM_FALLING_BLOCK_ENTITY_KEY =
            key("custom_falling_block_entity");

    public static final EntityType<CustomFallingBlockEntity> CUSTOM_FALLING_BLOCK_ENTITY =
            register(CUSTOM_FALLING_BLOCK_ENTITY_KEY,
                    EntityType
                            .Builder
                            .of(CustomFallingBlockEntity::new, MobCategory.MISC)
                            .build(CUSTOM_FALLING_BLOCK_ENTITY_KEY));

    //HELPER METHODS
    private static ResourceKey<EntityType<?>> key(String name) {
        return ResourceKey.create(
                Registries.ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(ModInitialization.MOD_ID, name));
    }

    private static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> key, EntityType<T> entityType) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, entityType);
    }

    //Initializes the entity
    public static void init() {}
}
