package com.Pseminar.Physics.Collider;

import org.jbox2d.collision.shapes.PolygonShape;

public class BoxCollider extends Collider {

	public BoxCollider(float width, float height) {
		shape = new PolygonShape();
		((PolygonShape) shape).setAsBox(width / 2, height / 2);
	}

}