package com.Pseminar.Physics;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

public class Physics2D {

	private final static int VELOCITYITERNATION = 6;
	private final static int POSITIONITERNATION = 2;
    
    private static Physics2D INSTANCE;

    private Vec2 gravity;
    private World world;

    private List<IContactListener> listeners = new ArrayList<>();
    
    public Physics2D(Vector2f gravity) {
        this.gravity = new Vec2(gravity.x(), gravity.y());
        world = new World(this.gravity);

        INSTANCE = this;

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                listeners.forEach(x -> x.OnContactBegin(contact));
            }

            @Override
            public void endContact(Contact contact) {
                listeners.forEach(x -> x.OnContactEnd(contact));
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                
            }
        });

    }

    /** 
     * @param gravity
     */
    public void SetGravit(Vector2f gravity) {
        this.gravity = new Vec2(gravity.x(), gravity.y());
        world.setGravity(this.gravity);
    }

    /** 
     * @param def
     * @return Body
     */
    public Body CreateBody(BodyDef def) {
        return world.createBody(def);
    }

    /** 
     * @param AContactListener
     */
    public void AddNewPhysicsListener(IContactListener AContactListener) {
        this.listeners.add(AContactListener);
    }

    /** 
     * @param AContactListener
     */
    public void RemovePhysicsListener(IContactListener AContactListener) {
        this.listeners.remove(AContactListener);
    }

    /** 
     * @param dt
     */
    public void update(float dt) {
		world.step(dt, VELOCITYITERNATION, POSITIONITERNATION);
    }

    /** 
     * @return Physics2D
     */
    public static Physics2D GetInstance() {
        return INSTANCE;
    }
}