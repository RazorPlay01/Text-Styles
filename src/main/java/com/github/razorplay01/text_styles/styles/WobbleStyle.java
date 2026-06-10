package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.TextStyles;
import com.github.razorplay01.text_styles.util.Transform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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
		private int position;

		public WobbleInstance(TextStyle type, int distance, int speed) {
			super(type);
			this.distance = distance;
			this.speed = Math.max(1, speed);
			this.offsets = TextStyles.rangeLoop(-distance, distance);
			this.position = -1;
		}

		@Override
		public void applyEffect(Transform transform, boolean start, float advance) {
			if (this.offsets.length == 0) {
				return;
			}

			if (start || this.position < 0) {
				long now = TextStyles.currentTimeMillis();
				float cycleProgress = (now % this.speed) / (float) this.speed;
				this.position = (int) (cycleProgress * this.offsets.length);
				if (this.position >= this.offsets.length) {
					this.position = this.offsets.length - 1;
				}
			}

			transform.translate(0, this.offsets[this.position]);

			this.position++;
			if (this.position >= this.offsets.length) {
				this.position = 0;
			}
		}
	}
}
