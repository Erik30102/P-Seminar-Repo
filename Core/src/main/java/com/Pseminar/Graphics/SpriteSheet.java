package com.Pseminar.Graphics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.Pseminar.Assets.Asset;

public class SpriteSheet extends Asset {
	private Texture texture;
	private transient List<Sprite> sprites;

	private int SpriteWidth;
	private int SpriteHeight;

	public SpriteSheet(Texture texture2d, int SpriteWidth, int SpriteHeight) {
		Init(texture2d, SpriteWidth, SpriteHeight);
	}

	/** 
	 * @param texture2d
	 * @param SpriteWidth
	 * @param SpriteHeight
	 */
	private void Init(Texture texture2d, int SpriteWidth, int SpriteHeight)
	{
		this.texture = texture2d;
		this.sprites = new ArrayList<Sprite>();

		this.SpriteHeight = SpriteHeight;
		this.SpriteWidth = SpriteWidth;

		for (int y = 0; y < texture.GetHeight() + 1 - SpriteHeight; y += SpriteHeight) {
			for (int x = 0; x < texture.GetWidth() + 1  - SpriteWidth; x += SpriteWidth) {
				float[] uv = new float[]{
					x / (float) texture.GetWidth(), (y + SpriteHeight) / (float) texture.GetHeight(),
					(x + SpriteWidth) / (float) texture.GetWidth(), y / (float) texture.GetHeight(),
					x / (float) texture.GetWidth(), y / (float) texture.GetHeight(),
					(x + SpriteWidth) / (float) texture.GetWidth(), (y + SpriteHeight) / (float) texture.GetHeight(),
                };
				sprites.add(new Sprite(texture, uv));
			}
		}
	}

	/** 
	 * @return List<Sprite>
	 */
	public List<Sprite> getSprites() {
		return sprites;
	}

	/** 
	 * @return Texture
	 */
	public Texture getTexture() {
		return texture;
	}

	/** 
	 * @return int
	 */
	public int getSpriteWidth() {
		return SpriteWidth;
	}

	/** 
	 * @return int
	 */
	public int getSpriteHeight() {
		return SpriteHeight;
	}

	/** 
	 * @return AssetType
	 */
	@Override
	public AssetType GetAssetType() {
		return AssetType.SPRITESHEET;
	}

	/** 
	 * @param getTile
	 * @return Sprite
	 */
	public Sprite getSprite(int getTile) {
		return sprites.get(getTile);
	}

    @Override
    public void OnDispose() {
        // TODO
    }

	/** 
	 * @param stream
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(java.io.ObjectInputStream stream)
        throws IOException, ClassNotFoundException {
		stream.defaultReadObject();

		Init(texture, SpriteWidth, SpriteHeight);
	}
	
	// TODO: custom serialization
}