package com.Pseminar.ECS.BuiltIn;

import com.Pseminar.ECS.Component;
import com.Pseminar.Graphics.Animation;
import com.Pseminar.Graphics.Sprite;

public class AnimationSpriteComponent extends Component {

    public Animation animation;

    private transient float countdown;
    private transient int count;

    public AnimationSpriteComponent() {

    }

    public void OnUpdate(float dt) {
        if(animation == null) return;

        countdown += dt;

        if(countdown >= animation.GetFrameDuration()) {
            countdown = 0;
            count++;
        }
    }

    public void SetAnimation(Animation a) {
        this.animation = a;
    }

    public Animation GetAnimation() {
        return this.animation;
    }

    public Sprite GetCurrentSprite() {
        if(animation == null) return null;
        
        return animation.GetSpriteAtKeyfarme(count);
    }

    @Override
    public ComponentType GetComponentType() {
        return ComponentType.AnimationComponent;
    }
    
}
