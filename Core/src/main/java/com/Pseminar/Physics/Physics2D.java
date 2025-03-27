package com.Pseminar.Physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;

public class Physics2D {

	private final static int VELOCITYITERNATION = 6;
	private final static int POSITIONITERNATION = 2;
    
    private static Physics2D INSTANCE;

    private Vec2 gravity;
    private World world;
    
    public Physics2D(Vector2f gravity) {
        this.gravity = new Vec2(gravity.x(), gravity.y());
        world = new World(this.gravity);

        INSTANCE = this;
    }

    public void SetGravit(Vector2f gravity) {
        this.gravity = new Vec2(gravity.x(), gravity.y());
        world.setGravity(this.gravity);
    }

    public Body CreateBody(BodyDef def) {
        return world.createBody(def);
    }

    public void update(float dt) {
		world.step(dt, VELOCITYITERNATION, POSITIONITERNATION);
    }

    public static Physics2D GetInstance() {
        return INSTANCE;
    }
}