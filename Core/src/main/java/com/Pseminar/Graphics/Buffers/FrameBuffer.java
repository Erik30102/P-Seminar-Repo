package com.Pseminar.Graphics.Buffers;

import org.lwjgl.opengl.GL30;

import com.Pseminar.Logger;
import com.Pseminar.Graphics.Texture;
import com.Pseminar.Graphics.Texture.TextureFiliteringMode;
import com.Pseminar.Graphics.Texture.TextureFormat;
import com.Pseminar.Graphics.Texture.TextureWrappingMode;

public class FrameBuffer {
    public enum FramebufferTextureFormat {
		None,
		RGBA8,
		DEPTH24STENCIL8
	}

	private int FramebufferID = -1;
	private int width, height;

	private Texture FboTexture;

	public FrameBuffer(int width, int height) {
		Resize(width, height);
	}

	/**
	 * Invalidate the framebuffer and create a new one
	 */
	public void Invalidate() {
		if (FramebufferID != -1) {
			GL30.glDeleteFramebuffers(FramebufferID);
			FboTexture.OnDispose();
		}

		FramebufferID = GL30.glGenFramebuffers();
		int rboId = GL30.glGenRenderbuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FramebufferID);

		FboTexture = new Texture(width, height, TextureFormat.RGBA8, TextureFiliteringMode.NEARST,
				TextureWrappingMode.CLAMP_TO_BORDER);

		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_TEXTURE_2D,
				FboTexture.GetTextureId(), 0);

		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, rboId);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH_COMPONENT32, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
				GL30.GL_RENDERBUFFER, rboId);

		if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) {
			Logger.error("Framebuffer not completed");
		}
		Unbind();
	}

	/**
	 * Set the size of the framebuffer and invalidate to take in effect
	 * 
	 * @param width  the height of the framebuffer
	 * @param height the width of the framebuffer
	 */
	public void Resize(int width, int height) {
		this.width = width;
		this.height = height;

		Invalidate();
	}

	/**
	 * @return The texture object on which the framebuffer draws
	 */
	public Texture GetTexture() {
		return FboTexture;
	}

	/**
	 * Bind the framebuffer to capchure all the draw calls
	 */
	public void Bind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FramebufferID);
	}

	/**
	 * unbind the framebuffer
	 */
	public void Unbind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}

	/**
	 * @return the current width of the framebuffer
	 */
	public int GetWidth() {
		return this.width;
	}

	/**
	 * @return the current height of the framebuffer
	 */
	public int GetHeight() {
		return this.height;
	}
}
