package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.TextStyles;
import com.github.razorplay01.text_styles.util.Transform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;

import java.util.Map;

/**
 * A text style that reveals characters one by one, with optional landing or snapping effects.
 */
public class TypewriterStyle extends TextStyle {
    private final MapCodec<TypewriterInstance> codec = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.INT.optionalFieldOf("delay", 100).forGetter(i -> i.delay),
            Codec.FLOAT.optionalFieldOf("land_distance", 0f).forGetter(i -> i.landDistance),
            Codec.FLOAT.optionalFieldOf("snap_distance", 0f).forGetter(i -> i.snapDistance)
    ).apply(inst, (delay, land, snap) -> new TypewriterInstance(this, delay, land, snap)));

    @Override
    public TextStyleInstance createInstance(Map<String, String> params) {
        int delay = Integer.parseInt(params.getOrDefault("delay", params.getOrDefault("dl", params.getOrDefault("value", "100"))));
        float landDistance = Float.parseFloat(params.getOrDefault("land", params.getOrDefault("l", "0")));
        float snapDistance = Float.parseFloat(params.getOrDefault("snap", params.getOrDefault("sn", "0")));

        return new TypewriterInstance(this, delay, landDistance, snapDistance);
    }

    @Override
    public MapCodec<? extends TextStyleInstance> getCodec() {
        return codec;
    }

    public static class TypewriterInstance extends TextStyleInstance {
        protected final int delay;
        protected final float landDistance;
        protected final float snapDistance;

        private final MutableInt showIndex = new MutableInt(0);
        private final MutableInt currentIndex = new MutableInt(0);
        private final MutableLong last = new MutableLong();

        public TypewriterInstance(TextStyle type, int delay, float landDistance, float snapDistance) {
            super(type);
            this.delay = delay;
            this.landDistance = landDistance;
            this.snapDistance = snapDistance;
        }

        @Override
        public boolean getHidden(boolean start) {
            if (showIndex.intValue() == -1) {
                return false;
            }
            long time = TextStyles.getTimeMs();
            if (start) {
                if (showIndex.intValue() > currentIndex.intValue()) {
                    showIndex.setValue(-1);
                    return false;
                }
                currentIndex.setValue(0);
                if (last.longValue() == 0) {
                    last.setValue(time);
                }
            } else {
                currentIndex.increment();
            }
            if (time - last.longValue() >= delay) {
                last.setValue(time);
                showIndex.increment();
            }
            return currentIndex.intValue() > showIndex.intValue();
        }

        @Override
        public void apply(Transform transform, boolean start, float advance) {
            if (showIndex.intValue() == -1) return;

            if (currentIndex.intValue() == showIndex.intValue()) {
                float progress = 1 - ((float) (TextStyles.getTimeMs() - last.longValue()) / delay);
                if (landDistance != 0) {
                    transform.translate(0, landDistance * progress);
                }
                if (snapDistance != 0) {
                    transform.translate(snapDistance * progress, 0);
                }
            }
        }
    }
}
