package com.Pseminar.ECS.BuiltIn;

import com.Pseminar.ECS.Component;
import com.Pseminar.Graphics.Sprite;

public class SpriteComponent extends Component {
    private Sprite sprite = null;

    public SpriteComponent() {
        
    }

    public void SetSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite GetSprite() {
        return sprite;
    }

    @Override
    public String toString() {
        return "RenderComponent[sprite=" + sprite + "]";
    }

    @Override
    public ComponentType GetComponentType() {
        return ComponentType.SpriteComponent;
    }
}
