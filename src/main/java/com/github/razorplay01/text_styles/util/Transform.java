package com.github.razorplay01.text_styles.util;

import org.joml.Matrix4f;

@SuppressWarnings("unused")
public interface Transform {

    default void translate(float t) {
        translate(t, t);
    }

    void translate(float x, float y);

    default void scale(float s) {
        scale(s, s);
    }

    void scale(float x, float y);

    default void scaleAt(float scaleX, float scaleY, float pX, float pY) {
        translate(pX, pY);
        scale(scaleX, scaleY);
        translate(-pX, -pY);
    }

    void rotate(float ang);

    default void rotateDegrees(float deg) {
        rotate((float) Math.toRadians(deg));
    }

    default void rotateAround(float ang, float pX, float pY) {
        translate(pX, pY);
        rotate(ang);
        translate(-pX, -pY);
    }

    void setColor(int color);

    int getColor();

    void setAlpha(float alpha);

    float getAlpha();

    class TransformImpl implements Transform {
        public final Matrix4f matrix;
        private int color;
        private float alpha = 1.0f;

        public TransformImpl(Matrix4f matrix, int color) {
            this.matrix = matrix;
            this.color = color;
        }
		public TransformImpl(Matrix4f matrix) {
			this.matrix = matrix;
			this.color = 0xfffffff;
		}

        @Override
        public void translate(float x, float y) {
            matrix.translate(x, y, 0);
        }

        @Override
        public void scale(float x, float y) {
            matrix.scale(x, y, 1);
        }

        @Override
        public void rotate(float ang) {
            matrix.rotate(ang, 0, 0, 1);
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
