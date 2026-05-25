package com.github.razorplay01.text_styles.mixin;

import com.github.razorplay01.text_styles.styles.TextStyle;
import com.github.razorplay01.text_styles.util.StyleExtension;
import com.github.razorplay01.text_styles.util.Transform;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;

//? >=1.21.10 {
import net.minecraft.client.gui.font.TextRenderable;
//? }

//? >1.21.3 {
@Mixin(Font.PreparedTextBuilder.class)
//? } elif >1.15.2 {
/*@Mixin(Font.StringRenderOutput.class)
*///? }
public abstract class FontPreparedTextBuilderMixin {

	//? >= 1.21.11 {
    @WrapOperation(method = "accept(ILnet/minecraft/network/chat/Style;Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;createGlyph(FFIILnet/minecraft/network/chat/Style;FF)Lnet/minecraft/client/gui/font/TextRenderable$Styled;"))
	private TextRenderable.Styled applyOffsets(BakedGlyph instance, float x, float y, int textColor, int shadowColor, Style style, float boldOffset, float shadowOffset, Operation<TextRenderable.Styled> original, @Local(argsOnly = true, name = "position") int position) {
	//? }
	//? <=1.21.1 {
	/*@WrapOperation(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;renderChar(Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;ZZFFFLorg/joml/Matrix4f;Lcom/mojang/blaze3d/vertex/VertexConsumer;FFFFI)V"))
	private void applyOffsets(Font instance, BakedGlyph bakedGlyph, boolean bold, boolean italic, float boldOffset, float x, float y, Matrix4f matrix4f, VertexConsumer
	consumer, float r, float g, float b, float a, int light, Operation<Void> original, @Local(argsOnly = true, ordinal = 0) int position, @Local(argsOnly = true) Style style, @Local GlyphInfo info) {
	*///? }

        @Nullable Collection<TextStyle.TextStyleInstance> textStyles = ((StyleExtension) (Object) style).getStyles();
        Matrix4f transformMatrix = null;

        boolean hide = false;
		//? >1.21.1{
        int currentColor = textColor;
		//?}

        if (textStyles != null && !textStyles.isEmpty()) {
            boolean start = position == 0;
            transformMatrix = new Matrix4f();
			//? >1.21.1{
            Transform.TransformImpl transform = new Transform.TransformImpl(transformMatrix ,textColor);
			//?}
			//? <=1.21.1{
            /*Transform.TransformImpl transform = new Transform.TransformImpl(transformMatrix);
			*///?}
            for (TextStyle.TextStyleInstance effect : textStyles) {
                if (effect.getHidden(start)) {
                    hide = true;
                    break;
                }
                effect.apply(transform, start, x);
            }

            // Apply color and alpha from transform
			//? >1.21.1{
            currentColor = transform.getColor();
            float alpha = transform.getAlpha();
            if (alpha < 1.0f) {
                int s = (int) (((currentColor >> 24) & 0xFF) * alpha);
                currentColor = (currentColor & 0xFFFFFF) | (s << 24);
            }
			//?}
        }

		//? > 1.21.8 {
		TextRenderable/*? >1.21.10 >>*/.Styled org = original.call(instance, x, y, currentColor, shadowColor, style, boldOffset, shadowOffset);
		return hide || org == null ? null : new com.github.razorplay01.text_styles.WrappedTextRenderable(org, transformMatrix);
		 //? }
		//? >1.14.4 && <1.21.1 {
		/*if (!hide) {
			//? >1.19.2 {
			if (transformMatrix != null) {
				matrix4f = transformMatrix.mul(matrix4f);
			}
			//? } else {
			*//*bakedGlyph.text_effects$setTransform(transform);
			 *//*//? }
			original.call(instance, bakedGlyph, bold, italic, boldOffset, x, y, matrix4f, consumer, r, g, b, a, light);
		}*/
		//? }
    }
}
