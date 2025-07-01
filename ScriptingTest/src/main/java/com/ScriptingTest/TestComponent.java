package com.ScriptingTest;

import org.lwjgl.glfw.GLFW;

import com.Pseminar.ECS.BuiltIn.BaseComponent;
import com.Pseminar.Window.Input;

public class TestComponent extends BaseComponent {

    public float speed = 2;

    @Override
    public void OnUpdate(float dt) {
        if(Input.IsKeyPressed(GLFW.GLFW_KEY_D)) {
            this.GetEntity().transform.move(10f * dt, 0);
        }
        if(Input.IsKeyPressed(GLFW.GLFW_KEY_W)) {
            this.GetEntity().transform.move(0.0f , 10f * dt);
        }
        if(Input.IsKeyPressed(GLFW.GLFW_KEY_S)) {
            this.GetEntity().transform.move(0.0f, -10f * dt);
        }
        if(Input.IsKeyPressed(GLFW.GLFW_KEY_A)) {
            this.GetEntity().transform.move(-10f * dt, 0);
        }
    }
}
