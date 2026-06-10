package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.TextStyles;
import com.github.razorplay01.text_styles.util.Transform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;

import java.util.Map;

/**
 * A text style that makes characters shake randomly.
 */
public class ShakeStyle extends TextStyle {
    private final MapCodec<ShakeInstance> codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.FLOAT.optionalFieldOf("x_distance", 2.0f).forGetter(i -> i.xDistance),
            Codec.FLOAT.optionalFieldOf("y_distance", 2.0f).forGetter(i -> i.yDistance),
            Codec.INT.optionalFieldOf("frequency", 1).forGetter(i -> i.frequency)
    ).apply(inst, (x, y, freq) -> new ShakeInstance(this, x, y, freq)));

    @Override
    public TextStyleInstance createInstance(Map<String, String> params) {
        float xDist = Float.parseFloat(params.getOrDefault("x", params.getOrDefault("distance", "2.0")));
        float yDist = Float.parseFloat(params.getOrDefault("y", params.getOrDefault("distance", "2.0")));
        int freq = Integer.parseInt(params.getOrDefault("frequency", params.getOrDefault("f", "1")));

        if (params.containsKey("value")) {
            xDist = Float.parseFloat(params.get("value"));
            yDist = xDist;
        }

        return new ShakeInstance(this, xDist, yDist, freq);
    }

    @Override
    public MapCodec<? extends TextStyleInstance> getCodec() {
        return codec;
    }

    public static class ShakeInstance extends TextStyleInstance {
        protected final float xDistance;
        protected final float yDistance;
        protected final int frequency;
        private final RandomSource random = RandomSource.create();

        public ShakeInstance(TextStyle type, float xDistance, float yDistance, int frequency) {
            super(type);
            this.xDistance = xDistance;
            this.yDistance = yDistance;
            this.frequency = Math.max(1, frequency);
        }

        @Override
        public void applyEffect(Transform transform, boolean start, float advance) {
            if ((TextStyles.currentTimeMillis() / 50) % frequency == 0) {
                transform.translate(
                        (random.nextFloat() - 0.5f) * 2 * xDistance,
                        (random.nextFloat() - 0.5f) * 2 * yDistance
                );
            }
        }
    }
}
