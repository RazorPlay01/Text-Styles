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

public class TextTransformWrapper implements TextRenderable.Styled {

	private final TextRenderable.Styled original;
	private final Matrix4f customTransform;

	public TextTransformWrapper(TextRenderable.Styled original, Matrix4f customTransform) {
		this.original = original;
		this.customTransform = customTransform;
	}

	@Override
	public float bottom() {
		return original.bottom();
	}

	@Override
	public float right() {
		return original.right();
	}

	@Override
	public float top() {
		return original.top();
	}

	@Override
	public float left() {
		return original.left();
	}

	@Override
	public RenderPipeline guiPipeline() {
		return original.guiPipeline();
	}

	@Override
	public GpuTextureView textureView() {
		return original.textureView();
	}

	@Override
	public RenderType renderType(Font.DisplayMode displayMode) {
		return original.renderType(displayMode);
	}

	@Override
	public void render(Matrix4fc pose, VertexConsumer buffer, int packedLightCoords, boolean flat) {
		Matrix4fc finalPose = (customTransform != null)
				? customTransform.mul(pose)
				: pose;
		original.render(finalPose, buffer, packedLightCoords, flat);
	}

	@Override
	public Style style() {
		return original.style();
	}
}
//? }
