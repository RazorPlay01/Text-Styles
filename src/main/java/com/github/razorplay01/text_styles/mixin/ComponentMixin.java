package com.github.razorplay01.text_styles.mixin;

import com.github.razorplay01.text_styles.TextStyleParser;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Component.class)
public interface ComponentMixin {

    @ModifyReturnValue(
            method = "literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;",
            at = @At("RETURN")
    )
    private static MutableComponent parseLiteral(MutableComponent original, String text) {
        if (text == null || !text.contains("<")) {
            return original;
        }
        return TextStyleParser.parse(text);
    }

    @ModifyReturnValue(
            method = "translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;",
            at = @At("RETURN")
    )
    private static MutableComponent parseTranslatable(MutableComponent original, String key, Object[] args) {
        if (key != null && key.contains("<")) {
            return TextStyleParser.parse(key);
        }
        return original;
    }
}
