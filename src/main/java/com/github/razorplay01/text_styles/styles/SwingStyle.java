package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.TextStyles;
import com.github.razorplay01.text_styles.util.Transform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;

import java.util.Map;

/**
 * A text style that makes characters swing/rotate back and forth.
 */
public class SwingStyle extends TextStyle {
    private final MapCodec<SwingInstance> codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.FLOAT.optionalFieldOf("speed", 0.005f).forGetter(i -> i.speed),
            Codec.FLOAT.optionalFieldOf("amplitude", 15.0f).forGetter(i -> i.amplitude),
            Codec.FLOAT.optionalFieldOf("delay", 0.05f).forGetter(i -> i.delay)
    ).apply(inst, (speed, amp, delay) -> new SwingInstance(this, speed, amp, delay)));

    @Override
    public TextStyleInstance createInstance(Map<String, String> params) {
        float speed = Float.parseFloat(params.getOrDefault("speed", params.getOrDefault("s", "0.005")));
        float amplitude = Float.parseFloat(params.getOrDefault("amplitude", params.getOrDefault("a", params.getOrDefault("value", "15.0"))));
        float delay = Float.parseFloat(params.getOrDefault("delay", params.getOrDefault("dl", "0.05")));

        return new SwingInstance(this, speed, amplitude, delay);
    }

    @Override
    public MapCodec<? extends TextStyleInstance> getCodec() {
        return codec;
    }

    public static class SwingInstance extends TextStyleInstance {
        protected final float speed;
        protected final float amplitude;
        protected final float delay;

        public SwingInstance(TextStyle type, float speed, float amplitude, float delay) {
            super(type);
            this.speed = speed;
            this.amplitude = amplitude;
            this.delay = delay;
        }

        @Override
        public void apply(Transform transform, boolean start, float advance) {
            long time = TextStyles.getTimeMs();
            float angle = Mth.sin(time * speed + advance * delay) * amplitude;
            transform.rotateAround((float) Math.toRadians(angle), 4, 8);
        }
    }
}
