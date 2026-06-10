package com.github.razorplay01.text_styles.util;

import org.joml.Matrix4f;

public interface Transform {

    void translate(float x, float y);

    void scale(float x, float y);

    default void scaleAt(float scaleX, float scaleY, float pX, float pY) {
        translate(pX, pY);
        scale(scaleX, scaleY);
        translate(-pX, -pY);
    }

    void rotate(float ang);

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
