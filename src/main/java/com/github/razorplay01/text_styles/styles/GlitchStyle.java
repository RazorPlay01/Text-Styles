package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.TextStyles;
import com.github.razorplay01.text_styles.util.Transform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;

import java.util.Map;

/**
 * A text style that makes characters glitch with random offsets and color shifts.
 */
public class GlitchStyle extends TextStyle {
    private final MapCodec<GlitchInstance> codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.FLOAT.optionalFieldOf("intensity", 1.0f).forGetter(i -> i.intensity),
            Codec.INT.optionalFieldOf("frequency", 5).forGetter(i -> i.frequency)
    ).apply(inst, (intensity, freq) -> new GlitchInstance(this, intensity, freq)));

    @Override
    public TextStyleInstance createInstance(Map<String, String> params) {
        float intensity = Float.parseFloat(params.getOrDefault("intensity", params.getOrDefault("i", params.getOrDefault("value", "1.0"))));
        int frequency = Integer.parseInt(params.getOrDefault("frequency", params.getOrDefault("f", "5")));

        return new GlitchInstance(this, intensity, frequency);
    }

    @Override
    public MapCodec<? extends TextStyleInstance> getCodec() {
        return codec;
    }

    public static class GlitchInstance extends TextStyleInstance {
        protected final float intensity;
        protected final int frequency;
        private final RandomSource random = RandomSource.create();

        public GlitchInstance(TextStyle type, float intensity, int frequency) {
            super(type);
            this.intensity = intensity;
            this.frequency = frequency;
        }

        @Override
        public void apply(Transform transform, boolean start, float advance) {
            long time = TextStyles.getTimeMs();
            if ((time / 50) % frequency == 0) {
                if (random.nextFloat() < 0.3f) {
                    transform.translate(
                            (random.nextFloat() - 0.5f) * 4 * intensity,
                            (random.nextFloat() - 0.5f) * 2 * intensity
                    );

                    if (random.nextFloat() < 0.5f) {
                        int r = random.nextInt(256);
                        int g = random.nextInt(256);
                        int b = random.nextInt(256);
                        int originalColor = transform.getColor();
                        int alpha = originalColor & 0xFF000000;
                        transform.setColor(alpha | (r << 16) | (g << 8) | b);
                    }
                }
            }
        }
    }
}
