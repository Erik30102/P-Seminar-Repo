package com.Pseminar.ECS.BuiltIn;

import com.Pseminar.BuiltIn.Tilemap;
import com.Pseminar.ECS.Component;
import com.Pseminar.Graphics.Sprite;

public class TilemapComponent extends Component {

    private Tilemap tilemap;

    @Override
    public ComponentType GetComponentType() {
        return ComponentType.TilemapComponent;
    }

    public Tilemap GetTilemap() {
        return this.tilemap;
    }

    public void SetTilemap(Tilemap tilemap) {
        this.tilemap = tilemap;
    }

    public Sprite GetTileAt(int x, int y) {
        return this.tilemap.GetSpritesheet().getSprite(this.tilemap.GetTile(x, y));
    }
}
