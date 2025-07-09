package com.ScriptingTest;

import org.lwjgl.glfw.GLFW;

import com.Pseminar.ECS.BuiltIn.BaseComponent;
import com.Pseminar.ECS.BuiltIn.RidgedBodyComponent;
import com.Pseminar.Window.Input;

public class TestComponent extends BaseComponent {

    public float movementSpeed;
    public float decellSpeed;

    private transient RidgedBodyComponent c;

    @Override
    public void OnStart() {
        c = this.GetEntity().GetComponent(RidgedBodyComponent.class);
        // Geht ned aus irgend nem grund TODO: fix 
    }

    @Override
    public void OnUpdate(float dt) {
        if(c != null) {
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

            this.move(deltaX, deltaY, dt);
        }else {
            c = this.GetEntity().GetComponent(RidgedBodyComponent.class);
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
