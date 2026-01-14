package com.fireball_stick.client;

import com.fireball_stick.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.world.entity.item.PrimedTnt;
public class ClientInitialization implements ClientModInitializer {
    //Needed since we need a renderer registered for the custom entities. Null otherwise, hard crashes
    @Override
    public void onInitializeClient() {
        EntityRenderers.register(
                ModEntities.CUSTOM_TNT,
                //Renders the CustomTnt like the vanilla TNT
                TntRenderer::new);
    }
}
