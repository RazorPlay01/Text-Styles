package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.TextStyles;
import com.github.razorplay01.text_styles.util.Transform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;

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

		private boolean animationFinished = false;
		private int allowedVisibleChars = 0;
		private int currentRenderPos = 0;
		private long lastRevealTime = 0L;

		public TypewriterInstance(TextStyle type, int delay, float landDistance, float snapDistance) {
			super(type);
			this.delay = delay;
			this.landDistance = landDistance;
			this.snapDistance = snapDistance;
		}

		@Override
		public boolean getHidden(boolean start) {
			if (this.animationFinished) return false;

			long currentTime = TextStyles.getTimeMs();

			if (start) {
				if (this.allowedVisibleChars > this.currentRenderPos) {
					this.animationFinished = true;
					return false;
				}
				this.currentRenderPos = 0;
				if (this.lastRevealTime == 0L) {
					this.lastRevealTime = currentTime;
				}
			} else {
				this.currentRenderPos++;
			}

			if (currentTime - this.lastRevealTime >= this.delay) {
				this.lastRevealTime = currentTime;
				this.allowedVisibleChars++;
			}

			return this.currentRenderPos > this.allowedVisibleChars;
		}

		@Override
		public void apply(Transform transform, boolean start, float advance) {
			if (this.animationFinished) return;

			if (this.currentRenderPos == this.allowedVisibleChars) {
				float timePassed = TextStyles.getTimeMs() - this.lastRevealTime;
				float progress = 1.0F - (timePassed / this.delay);

				if (this.landDistance != 0.0F) {
					transform.translate(0, this.landDistance * progress);
				}
				if (this.snapDistance != 0.0F) {
					transform.translate(this.snapDistance * progress, 0);
				}
			}
		}
	}
}
