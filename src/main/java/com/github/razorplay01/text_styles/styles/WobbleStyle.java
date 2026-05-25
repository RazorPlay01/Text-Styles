package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.TextStyles;
import com.github.razorplay01.text_styles.util.Transform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Map;

/**
 * A text style that makes characters wobble vertically.
 */
public class WobbleStyle extends TextStyle {
    private final MapCodec<WobbleInstance> codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.optionalFieldOf("distance", 3).forGetter(i -> i.distance),
            Codec.INT.optionalFieldOf("speed", 300).forGetter(i -> i.speed)
    ).apply(inst, (dist, speed) -> new WobbleInstance(this, dist, speed)));

    @Override
    public TextStyleInstance createInstance(Map<String, String> params) {
        int distance = Integer.parseInt(params.getOrDefault("value", params.getOrDefault("distance", params.getOrDefault("d", "3"))));
        int speed = Integer.parseInt(params.getOrDefault("speed", params.getOrDefault("s", "300")));

        return new WobbleInstance(this, distance, speed);
    }

    @Override
    public MapCodec<? extends TextStyleInstance> getCodec() {
        return codec;
    }

    public static class WobbleInstance extends TextStyleInstance {
        protected final int distance;
        protected final int speed;
        private final int[] offsets;
        private final MutableInt index = new MutableInt(-1);

        public WobbleInstance(TextStyle type, int distance, int speed) {
            super(type);
            this.distance = distance;
            this.speed = Math.max(1, speed);
            this.offsets = TextStyles.rangeLoop(-distance, distance);
        }

        @Override
        public void apply(Transform transform, boolean start, float advance) {
            int i = index.intValue();
            if (start || i == -1) {
                i = (int) (TextStyles.getTimeMs() % speed / (float) speed * offsets.length);
            }
            int offset = offsets[Math.min(i, offsets.length - 1)];
            index.setValue((i + 1) % offsets.length);
            transform.translate(0, offset);
        }
    }
}
