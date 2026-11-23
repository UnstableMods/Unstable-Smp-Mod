package com.unstable.ultimate.cosmetica;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class CosmeticaManager {
    private static final HttpClient HTTP = HttpClient.newHttpClient();
    private static final Map<UUID, Identifier> CUSTOM_SKINS = new ConcurrentHashMap<>();
    private static final Map<UUID, Identifier> CUSTOM_CAPES = new ConcurrentHashMap<>();

    public static void applyCosmeticaForPlayer(MinecraftClient client, UUID playerUuid, String usernameOrCape) {
        CompletableFuture.runAsync(() -> {
            try {
                HttpRequest capeReq = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.cosmetica.cc/get/cape/" + usernameOrCape + ".png"))
                        .header("User-Agent", "UnstableUltimateMod/1.0")
                        .build();
                HttpResponse<InputStream> capeResp = HTTP.send(capeReq, HttpResponse.BodyHandlers.ofInputStream());

                if (capeResp.statusCode() == 200) {
                    try (InputStream in = capeResp.body()) {
                        NativeImage image = NativeImage.read(in);
                        client.execute(() -> {
                            Identifier id = Identifier.of("unstable_ultimate", "cosmetica_cape_" + playerUuid.toString().replace("-", ""));
                            client.getTextureManager().registerTexture(id, new NativeImageBackedTexture(() -> id.toString(), image));
                            CUSTOM_CAPES.put(playerUuid, id);
                        });
                    }
                }
            } catch (Exception ignored) {
            }
        });
    }

    public static Identifier getSkin(UUID uuid) {
        return CUSTOM_SKINS.get(uuid);
    }

    public static Identifier getCape(UUID uuid) {
        return CUSTOM_CAPES.get(uuid);
    }
}
