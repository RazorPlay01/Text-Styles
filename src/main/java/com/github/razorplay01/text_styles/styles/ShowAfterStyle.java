package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.TextStyles;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;

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
		private final long creationTime;
		private boolean revealed;

		public ShowAfterInstance(TextStyle type, int delay) {
			super(type);
			this.delay = delay;
			this.creationTime = TextStyles.currentTimeMillis();
			this.revealed = false;
		}

		@Override
		public boolean shouldHide(boolean start) {
			if (this.revealed) return false;

			if (start && (TextStyles.currentTimeMillis() - this.creationTime > this.delay)) {
				this.revealed = true;
			}

			return !this.revealed;
		}
	}
}
