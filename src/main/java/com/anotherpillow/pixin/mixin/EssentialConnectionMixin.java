package com.anotherpillow.pixin.mixin;

import org.lwjgl.glfw.Callbacks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.URI;

@Pseudo
@Mixin(targets = "gg.essential.network.connectionmanager.Connection")
public class EssentialConnectionMixin {
    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "java/net/URI.create(Ljava/lang/String;)Ljava/net/URI;")
    )
    private static URI redirectUriCreate(String originalUri) {
        String modifiedUri = originalUri.replace("essential.gg", "pixie.rip");
        System.out.println("[Pixin] Original URI: " + originalUri + " , replacing with " + modifiedUri);
        System.out.println("[Pixin] Ignore the below warning from Essential. It may be ignored");
        return URI.create(modifiedUri);
    }

    // just in case the user is running an old version of essential too (e.g. embedded as a library)
    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(
            method = "<init>",
            at = @At(value = "INVOKE", target = "java/net/URI.create(Ljava/lang/String;)Ljava/net/URI;")
    )
    private static URI redirectUriCreateOld(String originalUri) {
        String modifiedUri = originalUri.replace("essential.gg", "pixie.rip");
        System.out.println("[Pixin] Original URI: " + originalUri + " , replacing with " + modifiedUri);
        System.out.println("[Pixin] Ignore the below warning from Essential. It may be ignored");
        return URI.create(modifiedUri);
    }
}