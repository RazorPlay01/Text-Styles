package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.TextStyles;
import com.github.razorplay01.text_styles.util.Transform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;

/**
 * A text style that makes characters move in a marquee fashion.
 */
public class MarqueeStyle extends TextStyle {
    private final MapCodec<MarqueeInstance> codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.optionalFieldOf("min", -3).forGetter(i -> i.min),
            Codec.INT.optionalFieldOf("max", 10).forGetter(i -> i.max),
            Codec.BOOL.optionalFieldOf("vertical", false).forGetter(i -> i.vertical)
    ).apply(inst, (min, max, vertical) -> new MarqueeInstance(this, min, max, vertical)));

    @Override
    public TextStyleInstance createInstance(Map<String, String> params) {
        int min = Integer.parseInt(params.getOrDefault("min", params.getOrDefault("m", "-3")));
        int max = Integer.parseInt(params.getOrDefault("max", params.getOrDefault("x", "10")));
        boolean vertical = Boolean.parseBoolean(params.getOrDefault("vertical", params.getOrDefault("v", "false")));

        return new MarqueeInstance(this, min, max, vertical);
    }

    @Override
    public MapCodec<? extends TextStyleInstance> getCodec() {
        return codec;
    }

    public static class MarqueeInstance extends TextStyleInstance {
        protected final int min;
        protected final int max;
        protected final boolean vertical;
        private final int[] offsets;

        public MarqueeInstance(TextStyle type, int min, int max, boolean vertical) {
            super(type);
            this.min = min;
            this.max = max;
            this.vertical = vertical;
            this.offsets = TextStyles.rangeLoop(min, max);
        }

        @Override
        public void applyEffect(Transform transform, boolean start, float advance) {
            int offset = offsets[(int) (TextStyles.currentTimeMillis() % 300 / 300f * offsets.length)];
            if (vertical) {
                transform.translate(0, offset);
            } else {
                transform.translate(offset, 0);
            }
        }
    }
}
