package com.explosion_wands.client;

import com.explosion_wands.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.*;

public class ClientInitialization implements ClientModInitializer {
    //Needed since we need a renderer registered for the custom entities. Null otherwise, hard crashes
    @Override
    public void onInitializeClient() {
        EntityRenderers.register(
                ModEntities.CUSTOM_TNT,
                //Renders the CustomTnt like the vanilla TNT
                TntRenderer::new);

        //Renders the CustomFallingBlockEntity like the vanilla falling block entity
        EntityRenderers.register(
                ModEntities.CUSTOM_FALLING_BLOCK_ENTITY,
                FallingBlockRenderer::new);
    }
}
