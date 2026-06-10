package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.util.Transform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;

/**
 * A text style that adds a custom shadow/offset color.
 */
public class ShadowStyle extends TextStyle {
    private final MapCodec<ShadowInstance> codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.optionalFieldOf("color", 0xFF000000).forGetter(i -> i.shadowColor),
            Codec.FLOAT.optionalFieldOf("x", 1.0f).forGetter(i -> i.offsetX),
            Codec.FLOAT.optionalFieldOf("y", 1.0f).forGetter(i -> i.offsetY)
    ).apply(inst, (color, x, y) -> new ShadowInstance(this, color, x, y)));

    @Override
    public TextStyleInstance createInstance(Map<String, String> params) {
        int color = (int) Long.parseLong(params.getOrDefault("color", params.getOrDefault("c", "FF000000")).replace("#", ""), 16);
        float x = Float.parseFloat(params.getOrDefault("x", "1.0"));
        float y = Float.parseFloat(params.getOrDefault("y", "1.0"));

        return new ShadowInstance(this, color, x, y);
    }

    @Override
    public MapCodec<? extends TextStyleInstance> getCodec() {
        return codec;
    }

    public static class ShadowInstance extends TextStyleInstance {
        protected final int shadowColor;
        protected final float offsetX;
        protected final float offsetY;

        public ShadowInstance(TextStyle type, int shadowColor, float offsetX, float offsetY) {
            super(type);
            this.shadowColor = shadowColor;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        @Override
        public void applyEffect(Transform transform, boolean start, float advance) {
            transform.translate(offsetX, offsetY);
        }
    }
}
