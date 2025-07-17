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

    /** 
     * @param dt
     */
    public void OnUpdate(float dt) {
        if(animation == null) return;

        countdown += dt;

        if(countdown >= animation.GetFrameDuration()) {
            countdown = 0;
            count++;
        }
    }

    /** 
     * @param a
     */
    public void SetAnimation(Animation a) {
        this.animation = a;
    }

    /** 
     * @return Animation
     */
    public Animation GetAnimation() {
        return this.animation;
    }

    /** 
     * @return Sprite
     */
    public Sprite GetCurrentSprite() {
        if(animation == null) return null;
        
        return animation.GetSpriteAtKeyfarme(count);
    }

    /** 
     * @return ComponentType
     */
    @Override
    public ComponentType GetComponentType() {
        return ComponentType.AnimationComponent;
    }
    
}
