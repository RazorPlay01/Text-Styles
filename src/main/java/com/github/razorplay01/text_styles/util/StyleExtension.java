package com.github.razorplay01.text_styles.util;

import com.github.razorplay01.text_styles.styles.TextStyle;
import net.minecraft.network.chat.Style;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public interface StyleExtension {
    default Style withStyle(TextStyle.TextStyleInstance style) {
        throw new UnsupportedOperationException("Mixin Failure");
    }

    default Style withStyles(TextStyle.TextStyleInstance... styles) {
        return withStyles(List.of(styles));
    }

    default Style withStyles(Collection<TextStyle.TextStyleInstance> styles) {
        throw new UnsupportedOperationException("Mixin Failure");
    }

    default Collection<TextStyle.TextStyleInstance> getStyles() {
        throw new UnsupportedOperationException("Mixin Failure");
    }
}
