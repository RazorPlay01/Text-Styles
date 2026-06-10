package com.github.razorplay01.text_styles.util;

import com.github.razorplay01.text_styles.styles.TextStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Processes and applies a collection of {@link TextStyle.TextStyleInstance} to a given
 * {@link Transform}, encapsulating the application logic away from the Mixin layer.
 *
 * <p>This class is responsible for:
 * <ul>
 *   <li>Determining if a character should be hidden based on active styles.</li>
 *   <li>Applying color, alpha, and transform modifications from each style.</li>
 * </ul>
 */
public final class StyleProcessor {

	private StyleProcessor() {
		// Utility class, no instances allowed
	}

	/**
	 * Processes all active styles for a single character and applies their effects
	 * to the given {@link Transform}.
	 *
	 * @param styles   the collection of active style instances, may be null or empty
	 * @param transform the transform to apply effects to
	 * @param isFirstCharacter whether this character is the first in the text sequence
	 * @param x        the x position of the character being rendered
	 * @return a {@link StyleApplicationResult} containing the outcome of processing
	 */
	public static StyleApplicationResult process(
			@Nullable Collection<TextStyle.TextStyleInstance> styles,
			@NotNull Transform.Impl transform,
			boolean isFirstCharacter,
			float x
	) {
		if (styles == null || styles.isEmpty()) {
			return StyleApplicationResult.noStyles();
		}

		for (TextStyle.TextStyleInstance style : styles) {
			if (style.shouldHide(isFirstCharacter)) {
				return StyleApplicationResult.hidden();
			}
			style.applyEffect(transform, isFirstCharacter, x);
		}

		return StyleApplicationResult.applied();
	}

	// -------------------------------------------------------------------------
	// Inner result class
	// -------------------------------------------------------------------------

	/**
	 * Immutable result object returned by {@link StyleProcessor#process}.
	 * Encapsulates whether the character should be hidden and whether
	 * any styles were actually applied.
	 */
	public static final class StyleApplicationResult {

		private static final StyleApplicationResult NO_STYLES = new StyleApplicationResult(false, false);
		private static final StyleApplicationResult HIDDEN    = new StyleApplicationResult(true,  false);
		private static final StyleApplicationResult APPLIED   = new StyleApplicationResult(false, true);

		private final boolean hidden;
		private final boolean stylesApplied;

		private StyleApplicationResult(boolean hidden, boolean stylesApplied) {
			this.hidden        = hidden;
			this.stylesApplied = stylesApplied;
		}

		// -- Factory methods --------------------------------------------------

		/** No styles were present; nothing was applied. */
		static StyleApplicationResult noStyles() {
			return NO_STYLES;
		}

		/** A style requested that this character be hidden. */
		static StyleApplicationResult hidden() {
			return HIDDEN;
		}

		/** All styles were applied successfully. */
		static StyleApplicationResult applied() {
			return APPLIED;
		}

		// -- Accessors --------------------------------------------------------

		/**
		 * Returns {@code true} if the character should be skipped during rendering.
		 */
		public boolean shouldHide() {
			return hidden;
		}

		/**
		 * Returns {@code true} if at least one style was applied to the transform.
		 */
		public boolean wereStylesApplied() {
			return stylesApplied;
		}

		@Override
		public String toString() {
			return "StyleApplicationResult{hidden=" + hidden +
					", stylesApplied=" + stylesApplied + "}";
		}
	}
}
