package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.TextStyles;
import com.github.razorplay01.text_styles.util.Transform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;

import java.util.Map;

/**
 * A text style that applies a color gradient across the text.
 */
public class GradientStyle extends TextStyle {
    private final MapCodec<GradientInstance> codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.optionalFieldOf("color1", 0xFFFFFFFF).forGetter(i -> i.color1),
            Codec.INT.optionalFieldOf("color2", 0xFF000000).forGetter(i -> i.color2),
            Codec.FLOAT.optionalFieldOf("frequency", 0.01f).forGetter(i -> i.frequency),
            Codec.FLOAT.optionalFieldOf("speed", 0.005f).forGetter(i -> i.speed)
    ).apply(inst, (c1, c2, freq, speed) -> new GradientInstance(this, c1, c2, freq, speed)));

    @Override
    public TextStyleInstance createInstance(Map<String, String> params) {
        int color1 = (int) Long.parseLong(params.getOrDefault("color1", params.getOrDefault("c1", "FFFFFFFF")).replace("#", ""), 16);
        int color2 = (int) Long.parseLong(params.getOrDefault("color2", params.getOrDefault("c2", "FF000000")).replace("#", ""), 16);
        float frequency = Float.parseFloat(params.getOrDefault("frequency", params.getOrDefault("f", "0.01")));
        float speed = Float.parseFloat(params.getOrDefault("speed", params.getOrDefault("s", "0.005")));

        return new GradientInstance(this, color1, color2, frequency, speed);
    }

    @Override
    public MapCodec<? extends TextStyleInstance> getCodec() {
        return codec;
    }

    public static class GradientInstance extends TextStyleInstance {
        protected final int color1;
        protected final int color2;
        protected final float frequency;
        protected final float speed;

        public GradientInstance(TextStyle type, int color1, int color2, float frequency, float speed) {
            super(type);
            this.color1 = color1;
            this.color2 = color2;
            this.frequency = frequency;
            this.speed = speed;
        }

        @Override
        public void apply(Transform transform, boolean start, float x) {
            long time = TextStyles.getTimeMs();
            // Use absolute x position for smooth gradient across characters
            float t = (Mth.sin((time * speed) + (x * frequency)) + 1.0f) / 2.0f;

            int r = (int) Mth.lerp(t, (color1 >> 16) & 0xFF, (color2 >> 16) & 0xFF);
            int g = (int) Mth.lerp(t, (color1 >> 8) & 0xFF, (color2 >> 8) & 0xFF);
            int b = (int) Mth.lerp(t, color1 & 0xFF, color2 & 0xFF);

            int originalColor = transform.getColor();
            int alpha = originalColor & 0xFF000000;
            transform.setColor(alpha | (r << 16) | (g << 8) | b);
        }
    }
}
