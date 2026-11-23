package com.unstable.ultimate.mixin;

import com.unstable.ultimate.cosmetica.CosmeticaManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class CosmeticaPlayerMixin {

    @Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
    private void unstable$overrideCosmeticaTextures(CallbackInfoReturnable<SkinTextures> cir) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        UUID uuid = player.getUuid();

        Identifier customSkin = CosmeticaManager.getSkin(uuid);
        Identifier customCape = CosmeticaManager.getCape(uuid);

        if (customSkin != null || customCape != null) {
            SkinTextures original = cir.getReturnValue();
            cir.setReturnValue(new SkinTextures(
                    customSkin != null ? customSkin : original.texture(),
                    original.textureUrl(),
                    customCape != null ? customCape : original.capeTexture(),
                    customCape != null ? customCape : original.elytraTexture(),
                    original.model(),
                    original.secure()
            ));
        }
    }
}
