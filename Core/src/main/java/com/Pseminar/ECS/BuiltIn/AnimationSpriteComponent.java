package com.Pseminar.ECS.BuiltIn;

import com.Pseminar.ECS.Component;
import com.Pseminar.Graphics.Animation;
import com.Pseminar.Graphics.Sprite;

public class AnimationSpriteComponent extends Component {

    public Animation animation;

    private transient float countdown;
    private transient int count;

    public void OnUpdate(float dt) {
        countdown += dt;

        if(countdown >= animation.GetFrameDuration()) {
            countdown = 0;
            count++;
        }
    }

    public Sprite GetCurrentSprite() {
        return animation.GetSpriteAtKeyfarme(count);
    }

    @Override
    public ComponentType GetComponentType() {
        return ComponentType.AnimationComponent;
    }
    
}
