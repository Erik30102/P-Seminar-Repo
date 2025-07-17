package com.ScriptingTest;

import org.lwjgl.glfw.GLFW;

import com.Pseminar.ECS.BuiltIn.AnimationSpriteComponent;
import com.Pseminar.ECS.BuiltIn.BaseComponent;
import com.Pseminar.ECS.BuiltIn.RidgedBodyComponent;
import com.Pseminar.Graphics.Animation;
import com.Pseminar.Window.Input;

public class TestComponent extends BaseComponent {

    public float movementSpeed;
    public float decellSpeed;

    public Animation vorwaerts;
    public Animation ruckwaerts;
    public Animation rechts;
    public Animation links;

    private transient RidgedBodyComponent c;
    private transient AnimationSpriteComponent anim;

    @Override
    public void OnStart() {
        c = this.GetEntity().GetComponent(RidgedBodyComponent.class);
        c.GetBody().SetUserData(this);
        anim = this.GetEntity().GetComponent(AnimationSpriteComponent.class);
        // Geht ned aus irgend nem grund TODO: fix 
    }

    @Override
    public void OnUpdate(float dt) {
        if(c != null && anim != null) {
            int deltaX = 0;
            int deltaY = 0;
            if (Input.IsKeyPressed(GLFW.GLFW_KEY_W)) {
                deltaY += 1;
            }
            if (Input.IsKeyPressed(GLFW.GLFW_KEY_S)) {
                deltaY -= 1;
            }
            if (Input.IsKeyPressed(GLFW.GLFW_KEY_A)) {
                deltaX -= 1;
            }
            if (Input.IsKeyPressed(GLFW.GLFW_KEY_D)) {
                deltaX += 1;
            }

            this.updateSprite(deltaX, deltaY);

            this.move(deltaX, deltaY, dt);
        }else {
            c = this.GetEntity().GetComponent(RidgedBodyComponent.class);
            anim = this.GetEntity().GetComponent(AnimationSpriteComponent.class);

            c.GetBody().SetUserData(this);
        }
    }

    private void updateSprite(int deltaX, int deltaY) {
        if(deltaY > 0) {
            this.anim.SetAnimation(vorwaerts);
        } else if(deltaY < 0) {
            this.anim.SetAnimation(ruckwaerts);
        } else if(deltaX > 0) {
            this.anim.SetAnimation(rechts);
        } else if(deltaX < 0) {
            this.anim.SetAnimation(links);
        } else {
            this.anim.SetAnimation(ruckwaerts);
        }
    }

    private void move(int deltaX, int deltaY, float dt) {
        float targetY = deltaY * this.movementSpeed;
        float targetX = deltaX * this.movementSpeed;

        float speedDiffY = targetY - c.GetBody().GetVelocity().y() * this.decellSpeed;
        float speedDiffX = targetX - c.GetBody().GetVelocity().x() * this.decellSpeed;

        float movementY = (float) Math.pow(Math.abs(speedDiffY) * 30, 0.9)
                * Math.signum(speedDiffY);
        float movementX = (float) Math.pow(Math.abs(speedDiffX) * 30, 0.9)
                * Math.signum(speedDiffX);

        c.GetBody().ApplyImpulse(movementX, movementY);
    }
}
