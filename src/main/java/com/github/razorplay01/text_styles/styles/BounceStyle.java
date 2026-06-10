package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.TextStyles;
import com.github.razorplay01.text_styles.util.Transform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;

import java.util.Map;

/**
 * A text style that makes characters bounce.
 */
public class BounceStyle extends TextStyle {
    private final MapCodec<BounceInstance> codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.FLOAT.optionalFieldOf("speed", 0.01f).forGetter(i -> i.speed),
            Codec.FLOAT.optionalFieldOf("amplitude", 5.0f).forGetter(i -> i.amplitude),
            Codec.FLOAT.optionalFieldOf("delay", 0.1f).forGetter(i -> i.delay)
    ).apply(inst, (speed, amp, delay) -> new BounceInstance(this, speed, amp, delay)));

    @Override
    public TextStyleInstance createInstance(Map<String, String> params) {
        float speed = Float.parseFloat(params.getOrDefault("speed", params.getOrDefault("s", "0.01")));
        float amplitude = Float.parseFloat(params.getOrDefault("amplitude", params.getOrDefault("a", params.getOrDefault("value", "5.0"))));
        float delay = Float.parseFloat(params.getOrDefault("delay", params.getOrDefault("dl", "0.1")));

        return new BounceInstance(this, speed, amplitude, delay);
    }

    @Override
    public MapCodec<? extends TextStyleInstance> getCodec() {
        return codec;
    }

    public static class BounceInstance extends TextStyleInstance {
        protected final float speed;
        protected final float amplitude;
        protected final float delay;

        public BounceInstance(TextStyle type, float speed, float amplitude, float delay) {
            super(type);
            this.speed = speed;
            this.amplitude = amplitude;
            this.delay = delay;
        }

        @Override
        public void applyEffect(Transform transform, boolean start, float advance) {
            long time = TextStyles.currentTimeMillis();
            float bounce = Math.abs(Mth.sin(time * speed + advance * delay)) * amplitude;
            transform.translate(0, -bounce);
        }
    }
}
