package com.Pseminar.Graphics;

import java.io.IOException;
import java.io.ObjectStreamException;

import com.Pseminar.Assets.Asset;
import com.Pseminar.Assets.ProjectInfo;

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

	/*
	 * Int AssetIdTexture
	 * float array
	 */

	// private void readObject(java.io.ObjectInputStream stream)
    // 	throws IOException, ClassNotFoundException {
    //      
    //     this.texture = ProjectInfo.GetProjectInfo().GetAssetManager().GetAsset(stream.readInt());
	//  	this.uv = (float[])stream.readObject();
    // }
// 
    // private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
    //    	stream.writeInt(this.texture.GetAssetId());
	// 	stream.writeObject(this.uv);
    // }
}
