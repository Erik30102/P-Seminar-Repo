package com.Pseminar.BuiltIn;

import com.Pseminar.Assets.Asset;
import com.Pseminar.Graphics.SpriteSheet;

public class Tilemap extends Asset {

    private SpriteSheet spriteSheet;
    private int width, height;
    private int[] tiles;

    public Tilemap(SpriteSheet spriteSheet, int width, int height, int[]tiles) {
        this.height = height;
        this.width = width;
        this.spriteSheet = spriteSheet;
        this.tiles = tiles;
    }

    public Tilemap(SpriteSheet spriteSheet, int width, int height) {
        this.height = height;
        this.width = width;
        this.spriteSheet = spriteSheet;
     
        this.tiles = new int[width * height];
    }

    public SpriteSheet GetSpritesheet() {
		return spriteSheet;
	}

	public int GetWidth() {
		return width;
	}

	public int GetHeight() {
		return height;
	}

	public int[] GetTiles() {
		return tiles;
	}

	public int GetTile(int x, int y) {
		return tiles[y * width + x];
	}

    public void SetTiles(int[] tiles) {
		this.tiles = tiles;
	}

    @Override
    public AssetType GetAssetType() {
        return AssetType.TILEMAP;
    }

    @Override
    public void OnDispose() {

    }

    public void SetTile(int x, int y, int currentSelectedSpriteIndex) {
        this.tiles[x+y*width] = currentSelectedSpriteIndex;
    }    
}
