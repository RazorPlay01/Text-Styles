package com.github.razorplay01.text_styles;

//? >1.21.1{

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.network.chat.Style;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public class WrappedTextRenderable implements TextRenderable.Styled {

    private final Styled delegate;
    public Matrix4f transform;

    public WrappedTextRenderable(Styled delegate, Matrix4f transform) {
        this.delegate = delegate;
        this.transform = transform;
    }

    @Override
    public float bottom() {
        return delegate.bottom();
    }

    @Override
    public float right() {
        return delegate.right();
    }

    @Override
    public float top() {
        return delegate.top();
    }

    @Override
    public float left() {
        return delegate.left();
    }

    @Override
    public RenderPipeline guiPipeline() {
        return delegate.guiPipeline();
    }

    @Override
    public GpuTextureView textureView() {
        return delegate.textureView();
    }

    @Override
    public RenderType renderType(Font.DisplayMode displayMode) {
        return delegate.renderType(displayMode);
    }


    @Override
    public void render(Matrix4fc pose, VertexConsumer buffer, int packedLightCoords, boolean flat) {
        delegate.render(transform == null ? pose : transform.mul(pose), buffer, packedLightCoords, flat);
    }

    @Override
    public Style style() {
        return delegate.style();
    }
}
//? }
