package com.Pseminar.ECS;

import java.util.Vector;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Transform {
    private final Vector2f position;
    private final Vector2f scale;
    private float rotation;

    public Transform(Vector2f position, Vector2f scale, float rotation) {
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
	}

	public Transform() {
		this(new Vector2f(0, 0), new Vector2f(1, 1), 0);
	}

	public void move(Vector2f vec) {
		this.position.add(vec);
	}

	public void move(float x, float y) {
		this.position.add(x, y);
	}

	public void setPosition(Vector2f vec) {
		this.position.set(vec);
	}

	public void setPosition(float x, float y) {
		this.position.set(x, y);
	}

    public float GetRotation() {
        return this.rotation;
    }

	public void rotate(float angle) {
		this.rotation += angle;
	}

	public void setRotation(float angle) {
		this.rotation = angle;
	}

	public Vector2f GetPosition() {
		return new Vector2f(position);
	}

	public Vector2f GetScale() {
		return new Vector2f(scale);
	}

	public void setScale(Vector2f scale) {
		this.scale.set(scale);
	}

	public void setScale(float x, float y) {
		this.scale.set(x, y);
	}

    public Matrix4f GenerateTransformMatrix() {
        Matrix4f transform = new Matrix4f();
        transform.identity().translate(this.position.x, this.position.y, -10).scale(this.scale.x, this.scale.y, 1).rotateZ((float)Math.toRadians(rotation));

        return transform;
    }
}
