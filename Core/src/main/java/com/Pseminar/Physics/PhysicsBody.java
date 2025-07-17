package com.Pseminar.Physics;

import java.util.Vector;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.joml.Vector2f;

import com.Pseminar.Physics.Collider.Collider;

public class PhysicsBody {
    private float density = 10;
	private float friction = 0;

	private transient Body body;
	public BodyType bodyType;

	public enum BodyType {
		DYNAMIC, STATIC, KINEMATIC
	}

	/** 
	 * @param bodyType
	 * @return BodyType
	 */
	private static org.jbox2d.dynamics.BodyType GetBodyType(BodyType bodyType) {
		switch (bodyType) {
			case DYNAMIC:
				return org.jbox2d.dynamics.BodyType.DYNAMIC;
			case STATIC:
				return org.jbox2d.dynamics.BodyType.STATIC;
			case KINEMATIC:
				return org.jbox2d.dynamics.BodyType.KINEMATIC;
			default:
				return org.jbox2d.dynamics.BodyType.STATIC;
		}
	}

	public PhysicsBody(BodyType bodyType) {
		this.bodyType = bodyType;

		BodyDef def = new BodyDef();
		def.type = GetBodyType(bodyType);

		body = Physics2D.GetInstance().CreateBody(def);
	}

	/**
	 * Fügt einen Collider zu dem gegebenen Physikkörper hinzu
	 * WICHTIG: Ein Collider ohne Körper funktioniert nicht,
	 * jedes Objekt mit einem Collider muss auch eine Ridgidbody-Komponente haben
	 * 
	 * @param collider
	 */
	public void AddCollider(Collider collider) {
		AddCollider(collider, false);
	}

	/** 
	 * @param collider
	 * @param isSensor
	 */
	public void AddCollider(Collider collider,boolean isSensor) {
		FixtureDef def = new FixtureDef();
		def.shape = collider.getShape();
		def.density = this.density;
		def.friction = this.friction;
		def.isSensor = true;

		this.body.createFixture(def);
	}

	/**
	 * @return die Dichte des Objekts, beeinflusst die Kollisionsauflösung
	 */
	public float getDensity() {
		return density;
	}

	/**
	 * @param density setzt die Dichte des Objekts
	 */
	public void setDensity(float density) {
		this.density = density;
	}

	/**
	 * @return die x-Koordinate des Objekts in der Physiksimulation
	 */
	public float GetX() {
		return body.getPosition().x;
	}

	/**
	 * @return die y-Koordinate des Objekts in der Physiksimulation
	 */
	public float GetY() {
		return body.getPosition().y;
	}

	/**
	 * @return die Position als Vektor in der Physiksimulation
	 */
	public Vector2f GetPosition() {
		return new Vector2f(GetX(), GetY());
	}

	/** 
	 * @param data
	 */
	public void SetUserData(Object data) {
		this.body.setUserData(data);
	}

	/** 
	 * @return Object
	 */
	public Object GetUserData() {
		return this.body.getUserData();
	}

	/**
	 * Wendet einen Impuls an der Position des Körpers an, sodass der Körper den Impuls selbst ausführt
	 * 
	 * @param impuls der 2D-Vektor, der den Impuls repräsentiert
	 */
	public void ApplyImpulse(Vector2f impuls) {
		body.applyForce(new Vec2(impuls.x(), impuls.y()), new Vec2(this.GetX(), this.GetY()));
	}

	/**
	 * Dasselbe wie ApplyImpulse, aber mit einer bestimmten Position, von der der Impuls ausgeht
	 * 
	 * @param impuls   der 2D-Vektor, der den Impuls repräsentiert
	 * @param location die 2D-Position, von der aus der Impuls angewendet wird
	 */
	public void ApplyImpulse(Vector2f impuls, Vector2f location) {
		body.applyForce(new Vec2(impuls.x(), impuls.y()), new Vec2(location.x(), location.y()));
	}

	/**
	 * Setzt die Position des Objekts auf den angegebenen Vektor
	 * 
	 * @param position die neue Position des Objekts
	 */
	public void SetPosition(Vector2f position) {
		body.setTransform(new Vec2(position.x(), position.y()), body.getAngle());
	}

	/** 
	 * @param verloicy
	 */
	public void SetVelocity(Vector2f verloicy) {
		body.setLinearVelocity(new Vec2(verloicy.x(), verloicy.y()));
	}

	/**
	 * Wendet einen Impuls an der Position des Körpers an, sodass der Körper den Impuls selbst ausführt
	 * 
	 * @param x die x-Komponente des Impulses
	 * @param y die y-Komponente des Impulses
	 */
	public void ApplyImpulse(float x, float y) {
		this.ApplyImpulse(new Vector2f(x, y));
	}

	/** 
	 * @return Vector2f
	 */
	public Vector2f GetVelocity() {
		return new Vector2f(this.body.getLinearVelocity().x, this.body.getLinearVelocity().y);
	}

    public void Destroy() {
		this.body.destroyFixture(this.body.getFixtureList());
	}
}
