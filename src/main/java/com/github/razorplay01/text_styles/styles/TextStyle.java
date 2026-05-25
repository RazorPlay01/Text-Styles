package com.github.razorplay01.text_styles.styles;

import com.github.razorplay01.text_styles.util.Transform;
import com.mojang.serialization.MapCodec;
import java.util.Map;

/**
 * Represents a type of text style effect.
 * This class is registered in the text style registry.
 */
public abstract class TextStyle {

    public abstract TextStyleInstance createInstance(Map<String, String> params);

    /**
     * Gets the codec for serializing and deserializing instances of this style.
     * This allows the style to define its own parameters internally.
     *
     * @return The map codec for this style's instances.
     */
    public abstract MapCodec<? extends TextStyleInstance> getCodec();

    /**
     * Base class for an instance of a text style effect.
     * Holds the parameters and logic for applying the effect.
     */
    public abstract static class TextStyleInstance {
        private final TextStyle type;

        public TextStyleInstance(TextStyle type) {
            this.type = type;
        }

        /**
         * @return The type of this style effect.
         */
        public TextStyle getType() {
            return type;
        }

        /**
         * Applies the text effect to a glyph transformation.
         *
         * @param transform The transformation handler for the current glyph.
         * @param start     Whether this is the first glyph of a continuous string.
         * @param advance   The horizontal advance of this glyph.
         */
        public void apply(Transform transform, boolean start, float advance) {
        }

        /**
         * Determines if the glyph should be hidden.
         *
         * @param start Whether this is the first glyph of a continuous string.
         * @return True if the glyph should be hidden, false otherwise.
         */
        public boolean getHidden(boolean start) {
            return false;
        }
    }
}
