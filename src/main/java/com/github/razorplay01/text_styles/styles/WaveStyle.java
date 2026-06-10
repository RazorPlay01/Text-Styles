package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.TextStyles;
import com.github.razorplay01.text_styles.util.Transform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;

import java.util.Map;

/**
 * A text style that makes characters move in a wave pattern.
 */
public class WaveStyle extends TextStyle {
    private final MapCodec<WaveInstance> codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.FLOAT.optionalFieldOf("frequency", 0.1f).forGetter(i -> i.frequency),
            Codec.FLOAT.optionalFieldOf("amplitude", 4.0f).forGetter(i -> i.amplitude),
            Codec.FLOAT.optionalFieldOf("speed", 0.01f).forGetter(i -> i.speed),
            Codec.BOOL.optionalFieldOf("vertical", true).forGetter(i -> i.vertical)
    ).apply(inst, (freq, amp, speed, vert) -> new WaveInstance(this, freq, amp, speed, vert)));

    @Override
    public TextStyleInstance createInstance(Map<String, String> params) {
        float frequency = Float.parseFloat(params.getOrDefault("frequency", params.getOrDefault("f", "0.1")));
        float amplitude = Float.parseFloat(params.getOrDefault("amplitude", params.getOrDefault("a", "4.0")));
        float speed = Float.parseFloat(params.getOrDefault("speed", params.getOrDefault("s", "0.01")));
        boolean vertical = Boolean.parseBoolean(params.getOrDefault("vertical", params.getOrDefault("v", "true")));

        if (params.containsKey("value")) amplitude = Float.parseFloat(params.get("value"));

        return new WaveInstance(this, frequency, amplitude, speed, vertical);
    }

    @Override
    public MapCodec<? extends TextStyleInstance> getCodec() {
        return codec;
    }

    public static class WaveInstance extends TextStyleInstance {
        protected final float frequency;
        protected final float amplitude;
        protected final float speed;
        protected final boolean vertical;

        public WaveInstance(TextStyle type, float frequency, float amplitude, float speed, boolean vertical) {
            super(type);
            this.frequency = frequency;
            this.amplitude = amplitude;
            this.speed = speed;
            this.vertical = vertical;
        }

        @Override
        public void applyEffect(Transform transform, boolean start, float advance) {
            long time = TextStyles.currentTimeMillis();
            float offset = Mth.sin(time * speed + advance * frequency) * amplitude;

            if (vertical) {
                transform.translate(0, offset);
            } else {
                transform.translate(offset, 0);
            }
        }
    }
}
