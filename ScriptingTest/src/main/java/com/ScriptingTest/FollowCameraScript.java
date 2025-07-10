package com.ScriptingTest;

import org.joml.Vector2f;

import com.Pseminar.ECS.Entity;
import com.Pseminar.ECS.BuiltIn.BaseComponent;

public class FollowCameraScript extends BaseComponent {
    
    private transient Entity player;

    @Override
    public void OnUpdate(float dt) {
        if(player == null) {
            player = this.GetEntity().GetScene().GetEntityByName("Player");
        } else {
            Vector2f differenceBetweenPlayerCamera = player.transform.GetPosition().sub(this.GetEntity().transform.GetPosition(), new Vector2f());

            differenceBetweenPlayerCamera.mul(dt*5);

            this.GetEntity().transform.move(differenceBetweenPlayerCamera);
        }
    }
}
