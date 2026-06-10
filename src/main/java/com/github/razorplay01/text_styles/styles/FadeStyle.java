package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.TextStyles;
import com.github.razorplay01.text_styles.util.Transform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;

import java.util.Map;

/**
 * A text style that makes characters fade in and out.
 */
public class FadeStyle extends TextStyle {
    private final MapCodec<FadeInstance> codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.FLOAT.optionalFieldOf("speed", 0.005f).forGetter(i -> i.speed),
            Codec.FLOAT.optionalFieldOf("min_alpha", 0.0f).forGetter(i -> i.minAlpha),
            Codec.FLOAT.optionalFieldOf("max_alpha", 1.0f).forGetter(i -> i.maxAlpha),
            Codec.FLOAT.optionalFieldOf("delay", 0.05f).forGetter(i -> i.delay)
    ).apply(inst, (speed, min, max, delay) -> new FadeInstance(this, speed, min, max, delay)));

    @Override
    public TextStyleInstance createInstance(Map<String, String> params) {
        float speed = Float.parseFloat(params.getOrDefault("speed", params.getOrDefault("s", "0.005")));
        float minAlpha = Float.parseFloat(params.getOrDefault("min_alpha", params.getOrDefault("m", "0.0")));
        float maxAlpha = Float.parseFloat(params.getOrDefault("max_alpha", params.getOrDefault("x", "1.0")));
        float delay = Float.parseFloat(params.getOrDefault("delay", params.getOrDefault("dl", "0.05")));

        return new FadeInstance(this, speed, minAlpha, maxAlpha, delay);
    }

    @Override
    public MapCodec<? extends TextStyleInstance> getCodec() {
        return codec;
    }

    public static class FadeInstance extends TextStyleInstance {
        protected final float speed;
        protected final float minAlpha;
        protected final float maxAlpha;
        protected final float delay;

        public FadeInstance(TextStyle type, float speed, float minAlpha, float maxAlpha, float delay) {
            super(type);
            this.speed = speed;
            this.minAlpha = minAlpha;
            this.maxAlpha = maxAlpha;
            this.delay = delay;
        }

        @Override
        public void applyEffect(Transform transform, boolean start, float advance) {
            long time = TextStyles.currentTimeMillis();
            float alphaRange = (maxAlpha - minAlpha) / 2.0f;
            float midAlpha = (maxAlpha + minAlpha) / 2.0f;
            float alpha = midAlpha + Mth.sin(time * speed + advance * delay) * alphaRange;

            transform.setAlpha(transform.getAlpha() * alpha);
        }
    }
}
