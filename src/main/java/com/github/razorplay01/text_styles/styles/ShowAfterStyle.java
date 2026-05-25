package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.TextStyles;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A text style that hides text for a certain duration.
 */
public class ShowAfterStyle extends TextStyle {
    private final MapCodec<ShowAfterInstance> codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.optionalFieldOf("delay", 0).forGetter(i -> i.delay)
    ).apply(inst, delay -> new ShowAfterInstance(this, delay)));

    @Override
    public TextStyleInstance createInstance(Map<String, String> params) {
        int delay = Integer.parseInt(params.getOrDefault("delay", params.getOrDefault("dl", params.getOrDefault("value", "0"))));
        return new ShowAfterInstance(this, delay);
    }

    @Override
    public MapCodec<? extends TextStyleInstance> getCodec() {
        return codec;
    }

    public static class ShowAfterInstance extends TextStyleInstance {
        protected final int delay;
        private final long startTime;
        private final AtomicBoolean hidden = new AtomicBoolean(true);

        public ShowAfterInstance(TextStyle type, int delay) {
            super(type);
            this.delay = delay;
            this.startTime = TextStyles.getTimeMs();
        }

        @Override
        public boolean getHidden(boolean start) {
            if (!hidden.get()) return false;
            if (start) {
                if (TextStyles.getTimeMs() - startTime > delay) {
                    hidden.set(false);
                }
            }
            return hidden.get();
        }
    }
}
