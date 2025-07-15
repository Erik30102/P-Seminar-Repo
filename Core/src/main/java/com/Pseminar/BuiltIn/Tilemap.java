package com.Pseminar.BuiltIn;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import com.Pseminar.Assets.Asset;
import com.Pseminar.Graphics.SpriteSheet;
import com.Pseminar.Physics.PhysicsBody;
import com.Pseminar.Physics.Collider.BoxCollider;
import com.Pseminar.Physics.PhysicsBody.BodyType;

public class Tilemap extends Asset {

    private SpriteSheet spriteSheet;
    private int width, height;
    private int[] tiles;

    private boolean[] isSolid;

    public Tilemap(SpriteSheet spriteSheet, int width, int height, int[]tiles, boolean[] isSolid) {
        this.height = height;
        this.width = width;
        this.spriteSheet = spriteSheet;
        this.tiles = tiles;
        this.isSolid = isSolid;
    }

    public Tilemap(SpriteSheet spriteSheet, int width, int height) {
        this.height = height;
        this.width = width;
        this.spriteSheet = spriteSheet;
     
        this.tiles = new int[width * height];
        this.isSolid = new boolean[width * height];
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

    public boolean[] GetCollidingMap() {
        return this.isSolid;
    }

    public boolean IsTileSolid(int x, int y) {
        return this.isSolid[y * width + x];
    }

    public void SetTileIsSolid(int x, int y, boolean isSolid) {
        this.isSolid[y * width + x] = isSolid;
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

    private transient List<PhysicsBody> colliders = new ArrayList<>();

    public void InitPhysics(Vector2f offset) {
        if(colliders == null)
            colliders = new ArrayList<>();

        for(int x = 0; x < this.width; x++) {
            for(int y = 0; y < this.height; y++) {
                if(IsTileSolid(x, y)) {
                    PhysicsBody p = new PhysicsBody(BodyType.STATIC);
                    p.AddCollider(new BoxCollider(1, 1));
                    p.SetPosition(new Vector2f(offset.x + x, offset.y + y));

                    colliders.add(p);
                }
            }
        }
    }

    public void RemoveFromPhysics() {
        if(colliders == null)
            return;

        for (PhysicsBody physicsBody : colliders) {
            physicsBody.Destroy();
        }
    }    
}
