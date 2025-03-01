package com.Pseminar.Physics;

import org.jbox2d.common.*;
import org.jbox2d.callbacks.*;
import org.jbox2d.collision.*;
import org.jbox2d.dynamics.*;;

public class Physics2D {



    //Gravitation
    private float gravity_x = 0.0f;
    private float gravity_y = 10.0f;
    
    private Vec2 gravity = new Vec2(gravity_x, gravity_y);

    //Welt (Idk warum)
    private World world = new World(gravity);
    
    public void update(float time)
    {
        
    }
}