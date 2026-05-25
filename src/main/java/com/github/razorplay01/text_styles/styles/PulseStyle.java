package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.TextStyles;
import com.github.razorplay01.text_styles.util.Transform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;

import java.util.Map;

/**
 * A text style that makes characters pulse in scale or color.
 */
public class PulseStyle extends TextStyle {
    private final MapCodec<PulseInstance> codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.FLOAT.optionalFieldOf("speed", 0.01f).forGetter(i -> i.speed),
            Codec.FLOAT.optionalFieldOf("intensity", 0.2f).forGetter(i -> i.intensity),
            Codec.FLOAT.optionalFieldOf("delay", 0.05f).forGetter(i -> i.delay),
            Codec.INT.optionalFieldOf("color", -1).forGetter(i -> i.pulseColor)
    ).apply(inst, (speed, intensity, delay, color) -> new PulseInstance(this, speed, intensity, delay, color)));

    @Override
    public TextStyleInstance createInstance(Map<String, String> params) {
        float speed = Float.parseFloat(params.getOrDefault("speed", params.getOrDefault("s", "0.01")));
        float intensity = Float.parseFloat(params.getOrDefault("intensity", params.getOrDefault("i", params.getOrDefault("value", "0.2"))));
        float delay = Float.parseFloat(params.getOrDefault("delay", params.getOrDefault("dl", "0.05")));
        int color = (params.containsKey("color") || params.containsKey("c"))
                ? (int) Long.parseLong(params.getOrDefault("color", params.getOrDefault("c", "FFFFFFFF")).replace("#", ""), 16)
                : -1;

        return new PulseInstance(this, speed, intensity, delay, color);
    }

    @Override
    public MapCodec<? extends TextStyleInstance> getCodec() {
        return codec;
    }

    public static class PulseInstance extends TextStyleInstance {
        protected final float speed;
        protected final float intensity;
        protected final float delay;
        protected final int pulseColor;

        public PulseInstance(TextStyle type, float speed, float intensity, float delay, int pulseColor) {
            super(type);
            this.speed = speed;
            this.intensity = intensity;
            this.delay = delay;
            this.pulseColor = pulseColor;
        }

        @Override
        public void apply(Transform transform, boolean start, float advance) {
            long time = TextStyles.getTimeMs();
            float t = (Mth.sin(time * speed + advance * delay) + 1.0f) / 2.0f;

            float scale = 1.0f + (t * intensity);
            transform.scaleAt(scale, scale, 4, 4);

            if (pulseColor != -1) {
                int originalColor = transform.getColor();
                int r = (int) Mth.lerp(t, (originalColor >> 16) & 0xFF, (pulseColor >> 16) & 0xFF);
                int g = (int) Mth.lerp(t, (originalColor >> 8) & 0xFF, (pulseColor >> 8) & 0xFF);
                int b = (int) Mth.lerp(t, originalColor & 0xFF, pulseColor & 0xFF);
                int alpha = originalColor & 0xFF000000;
                transform.setColor(alpha | (r << 16) | (g << 8) | b);
            }
        }
    }
}
