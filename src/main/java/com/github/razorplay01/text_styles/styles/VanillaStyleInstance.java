package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.util.Transform;
import net.minecraft.network.chat.Style;

public class VanillaStyleInstance extends TextStyle.TextStyleInstance {
	private final Style vanillaStyle;

	public VanillaStyleInstance(Style style) {
		super(null);
		this.vanillaStyle = style;
	}

	@Override
	public void apply(Transform transform, boolean start, float advance) {
		// Los estilos vanilla no transforman glyphs
	}

	public Style getVanillaStyle() {
		return vanillaStyle;
	}
}
