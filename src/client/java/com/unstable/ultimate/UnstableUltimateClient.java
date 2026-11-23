package com.unstable.ultimate;

import com.unstable.ultimate.cosmetica.CosmeticaManager;
import com.unstable.ultimate.gui.UnstableMenuHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public class UnstableUltimateClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(UnstableMenuHandler.CosmeticaSelectPayload.ID, (payload, context) -> {
            MinecraftClient client = context.client();
            if (client.player != null) {
                CosmeticaManager.applyCosmeticaForPlayer(client, client.player.getUuid(), payload.usernameOrCape());
            }
        });
    }
}
