package com.github.razorplay01.text_styles.mixin;

import com.github.razorplay01.text_styles.TextStyles;
import com.github.razorplay01.text_styles.styles.TextStyle;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
public abstract class BuiltInRegistriesMixin {
    @Shadow
    @Final
    private static WritableRegistry<WritableRegistry<?>> WRITABLE_REGISTRY;

    @SuppressWarnings("unchecked")
    @Inject(method = "bootStrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/registries/BuiltInRegistries;freeze()V"))
    private static void bootstrapRegistry(CallbackInfo ci) {
        var registry = (WritableRegistry<TextStyle>) TextStyles.REGISTRY;
        WRITABLE_REGISTRY.register((ResourceKey<WritableRegistry<?>>) (Object) registry.key(), registry, RegistrationInfo.BUILT_IN);
    }

    @Inject(method = "freeze", at = @At("TAIL"))
    private static void freezeRegistry(CallbackInfo ci) {
        TextStyles.REGISTRY.freeze();
    }
}
