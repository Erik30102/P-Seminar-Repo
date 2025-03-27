package com.Pseminar.Physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class Physics2D {

    private float gravity_x = 0.0f;
    private float gravity_y = 10.0f;

    private Vec2 gravity = new Vec2(gravity_x, gravity_y);

    private World world = new World(gravity);

    public void update(float time)
    {      

    }
}