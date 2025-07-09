package com.ScriptingTest;

import org.lwjgl.glfw.GLFW;

import com.Pseminar.ECS.BuiltIn.BaseComponent;
import com.Pseminar.ECS.BuiltIn.RidgedBodyComponent;
import com.Pseminar.Window.Input;

public class TestComponent extends BaseComponent {

    private transient RidgedBodyComponent c;

    @Override
    public void OnStart() {
        c = this.GetEntity().GetComponent(RidgedBodyComponent.class);
        // Geht ned aus irgend nem grund TODO: fix 
    }

    @Override
    public void OnUpdate(float dt) {
        if(c != null) {
            if(Input.IsKeyPressed(GLFW.GLFW_KEY_D)) {
                c.GetBody().ApplyImpulse(500f * dt, 0);
            }
            if(Input.IsKeyPressed(GLFW.GLFW_KEY_W)) {
                c.GetBody().ApplyImpulse(0.0f , 500f * dt);
            }
            if(Input.IsKeyPressed(GLFW.GLFW_KEY_S)) {
                c.GetBody().ApplyImpulse(0.0f, -500f * dt);
            }
            if(Input.IsKeyPressed(GLFW.GLFW_KEY_A)) {
                c.GetBody().ApplyImpulse(-500f * dt, 0);
            }
        }else {
            c = this.GetEntity().GetComponent(RidgedBodyComponent.class);
        }
    }
            
}
