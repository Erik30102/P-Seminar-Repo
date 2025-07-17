package com.Pseminar.ECS.BuiltIn;

import com.Pseminar.ECS.Component;
import com.Pseminar.Graphics.Sprite;

public class SpriteComponent extends Component {
    private Sprite sprite = null;

    public SpriteComponent() {
        
    }

    /** 
     * @param sprite
     */
    public void SetSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    /** 
     * @return Sprite
     */
    public Sprite GetSprite() {
        return sprite;
    }

    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "RenderComponent[sprite=" + sprite + "]";
    }

    /** 
     * @return ComponentType
     */
    @Override
    public ComponentType GetComponentType() {
        return ComponentType.SpriteComponent;
    }
}
