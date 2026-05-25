package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.TextStyles;
import com.github.razorplay01.text_styles.util.Transform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import java.util.Map;

/**
 * A text style that makes characters move with random turbulence.
 */
public class TurbulenceStyle extends TextStyle {
    private final MapCodec<TurbulenceInstance> codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.FLOAT.optionalFieldOf("intensity", 1.0f).forGetter(i -> i.intensity),
            Codec.FLOAT.optionalFieldOf("speed", 0.005f).forGetter(i -> i.speed)
    ).apply(inst, (intensity, speed) -> new TurbulenceInstance(this, intensity, speed)));

    @Override
    public TextStyleInstance createInstance(Map<String, String> params) {
        float intensity = Float.parseFloat(params.getOrDefault("intensity", params.getOrDefault("i", params.getOrDefault("value", "1.0"))));
        float speed = Float.parseFloat(params.getOrDefault("speed", params.getOrDefault("s", "0.005")));

        return new TurbulenceInstance(this, intensity, speed);
    }

    @Override
    public MapCodec<? extends TextStyleInstance> getCodec() {
        return codec;
    }

    public static class TurbulenceInstance extends TextStyleInstance {
        protected final float intensity;
        protected final float speed;
        private final RandomSource random = RandomSource.create();

        public TurbulenceInstance(TextStyle type, float intensity, float speed) {
            super(type);
            this.intensity = intensity;
            this.speed = speed;
        }

        @Override
        public void apply(Transform transform, boolean start, float advance) {
            long time = TextStyles.getTimeMs();
            float seed = time * speed + advance;
            float ox = (Mth.sin(seed * 0.7f) + Mth.sin(seed * 1.3f)) * intensity;
            float oy = (Mth.cos(seed * 0.9f) + Mth.cos(seed * 1.1f)) * intensity;

            transform.translate(ox, oy);
        }
    }
}
