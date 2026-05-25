package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.TextStyles;
import com.github.razorplay01.text_styles.util.Transform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.awt.*;
import java.util.Map;

/**
 * A text style that cycles through rainbow colors.
 */
public class RainbowStyle extends TextStyle {
    private final MapCodec<RainbowInstance> codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.FLOAT.optionalFieldOf("frequency", 0.01f).forGetter(i -> i.frequency),
            Codec.FLOAT.optionalFieldOf("speed", 0.005f).forGetter(i -> i.speed),
            Codec.FLOAT.optionalFieldOf("saturation", 1.0f).forGetter(i -> i.saturation),
            Codec.FLOAT.optionalFieldOf("brightness", 1.0f).forGetter(i -> i.brightness)
    ).apply(inst, (freq, speed, sat, bri) -> new RainbowInstance(this, freq, speed, sat, bri)));

    @Override
    public TextStyleInstance createInstance(Map<String, String> params) {
        float frequency = Float.parseFloat(params.getOrDefault("frequency", params.getOrDefault("f", "0.01")));
        float speed = Float.parseFloat(params.getOrDefault("speed", params.getOrDefault("s", "0.005")));
        float saturation = Float.parseFloat(params.getOrDefault("saturation", params.getOrDefault("sat", "1.0")));
        float brightness = Float.parseFloat(params.getOrDefault("brightness", params.getOrDefault("bri", "1.0")));

        // Shortcut for speed if only one value is provided
        if (params.containsKey("value")) speed = Float.parseFloat(params.get("value"));

        return new RainbowInstance(this, frequency, speed, saturation, brightness);
    }

    @Override
    public MapCodec<? extends TextStyleInstance> getCodec() {
        return codec;
    }

    public static class RainbowInstance extends TextStyleInstance {
        protected final float frequency;
        protected final float speed;
        protected final float saturation;
        protected final float brightness;

        public RainbowInstance(TextStyle type, float frequency, float speed, float saturation, float brightness) {
            super(type);
            this.frequency = frequency;
            this.speed = speed;
            this.saturation = saturation;
            this.brightness = brightness;
        }

        @Override
        public void apply(Transform transform, boolean start, float x) {
            long time = TextStyles.getTimeMs();
            // x is the absolute horizontal position
            float hue = ((time * speed) + (x * frequency)) % 1.0f;
            if (hue < 0) hue += 1.0f;

            int color = Color.HSBtoRGB(hue, saturation, brightness);

            int originalColor = transform.getColor();
            int alpha = originalColor & 0xFF000000;
            transform.setColor(alpha | (color & 0xFFFFFF));
        }
    }
}
