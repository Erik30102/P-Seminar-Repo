package com.Pseminar.Graphics;

public class Sprite {
    private Texture texture;
	private float[] uv;

	public Sprite(Texture texture, float[] uv) {
		this.texture = texture;
		this.uv = uv;
	}

	public Texture getTexture() {
		return texture;
	}

	public float[] getUv() {
		return uv;
	}
}
