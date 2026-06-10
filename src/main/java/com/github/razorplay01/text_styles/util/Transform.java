package com.github.razorplay01.text_styles.util;

import org.joml.Matrix4f;

/**
 * Transform utility for text effects.
 * Reimplemented independently. Inspired by owlsys/text-effects (LGPL).
 */
public interface Transform {

	void translate(float x, float y);

	void scale(float x, float y);

	/**
	 * Scale around a pivot point.
	 */
	default void scaleAround(float scaleX, float scaleY, float pivotX, float pivotY) {
		translate(pivotX, pivotY);
		scale(scaleX, scaleY);
		translate(-pivotX, -pivotY);
	}

	void rotate(float angle);

	/**
	 * Rotate around a pivot point.
	 */
	default void rotateAround(float angle, float pivotX, float pivotY) {
		translate(pivotX, pivotY);
		rotate(angle);
		translate(-pivotX, -pivotY);
	}

	void setColor(int color);

	int getColor();

	void setAlpha(float alpha);

	float getAlpha();

	/**
	 * Default implementation.
	 */
	class Impl implements Transform {
		private final Matrix4f matrix;
		private int color;
		private float alpha = 1.0f;

		public Impl(Matrix4f matrix, int initialColor) {
			this.matrix = matrix;
			this.color = initialColor;
		}

		@Override
		public void translate(float x, float y) {
			matrix.translate(x, y, 0.0f);
		}

		@Override
		public void scale(float x, float y) {
			matrix.scale(x, y, 1.0f);
		}

		@Override
		public void rotate(float angle) {
			matrix.rotateZ(angle);
		}

		@Override
		public void setColor(int color) {
			this.color = color;
		}

		@Override
		public int getColor() {
			return color;
		}

		@Override
		public void setAlpha(float alpha) {
			this.alpha = alpha;
		}

		@Override
		public float getAlpha() {
			return alpha;
		}
	}
}
