package com.github.razorplay01.text_styles.mixin;

import com.github.razorplay01.text_styles.WrappedTextRenderable;
import com.github.razorplay01.text_styles.styles.TextStyle;
import com.github.razorplay01.text_styles.util.StyleExtension;
import com.github.razorplay01.text_styles.util.Transform;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;

@Mixin(Font.PreparedTextBuilder.class)
public abstract class FontPreparedTextBuilderMixin {

    @WrapOperation(method = "accept(ILnet/minecraft/network/chat/Style;Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;createGlyph(FFIILnet/minecraft/network/chat/Style;FF)Lnet/minecraft/client/gui/font/TextRenderable$Styled;"))
    private TextRenderable.Styled applyOffsets(BakedGlyph instance, float x, float y, int textColor, int shadowColor, Style style, float boldOffset, float shadowOffset, Operation<TextRenderable.Styled> original, @Local(argsOnly = true, name = "position") int position) {

        @Nullable Collection<TextStyle.TextStyleInstance> textStyles = ((StyleExtension) (Object) style).getStyles();
        Matrix4f transformMatrix = null;

        boolean hide = false;
        int currentColor = textColor;
        if (textStyles != null && !textStyles.isEmpty()) {
            boolean start = position == 0;
            transformMatrix = new Matrix4f();
            Transform.TransformImpl transform = new Transform.TransformImpl(transformMatrix, textColor);
            for (TextStyle.TextStyleInstance effect : textStyles) {
                if (effect.getHidden(start)) {
                    hide = true;
                    break;
                }
                effect.apply(transform, start, x);
            }

            // Apply color and alpha from transform
            currentColor = transform.getColor();
            float alpha = transform.getAlpha();
            if (alpha < 1.0f) {
                int a = (int) (((currentColor >> 24) & 0xFF) * alpha);
                currentColor = (currentColor & 0xFFFFFF) | (a << 24);
            }
        }

        TextRenderable.Styled org = original.call(instance, x, y, currentColor, shadowColor, style, boldOffset, shadowOffset);
        return hide || org == null ? null : new WrappedTextRenderable(org, transformMatrix);
    }
}
