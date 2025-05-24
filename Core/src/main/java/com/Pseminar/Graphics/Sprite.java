package com.Pseminar.Graphics;

import com.Pseminar.Assets.Asset;

public class Sprite extends Asset {
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

	@Override
	public AssetType GetAssetType() {
		return AssetType.SPRITE;
	}

	@Override
	public void OnDispose() {

	}
}
