package com.fireball_stick.client;

import com.fireball_stick.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import javax.naming.Context;

public class ClientInitialization implements ClientModInitializer {
    //Needed since we need a renderer registered for the custom entities. Null otherwise, hard crashes
    @Override
    public void onInitializeClient() {
        EntityRenderers.register(
                ModEntities.CUSTOM_TNT,
                //Renders the CustomTnt like the vanilla TNT
                TntRenderer::new);
/*
        EntityRenderers.register(
                ModEntities.CUSTOM_FIREBALL,
                (Context ctx) -> new FlyingItemEntityRenderer<>(ctx, fireball_stick_shotgun_air.png, 1.0f);
        );
 */

        EntityRenderers.register(
                ModEntities.CUSTOM_FALLING_BLOCK_ENTITY,
                FallingBlockRenderer::new);
    }
}
