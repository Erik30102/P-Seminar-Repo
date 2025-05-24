package com.Pseminar.ECS;

import org.joml.Matrix4f;
import org.joml.Vector2f;

/**
 * Die Klasse Transform hält einfach die 2D-Transformationswerte:
 * Position, Skalierung und Rotation.
 */
public class Transform {
    private final Vector2f position;
    private final Vector2f scale;
    private float rotation;

    /**
     * Initialisiert die Transform mit eigener Position, Scale und Rotation.
     *
     * @param position Startposition
     * @param scale Start-Scale
     * @param rotation Start-Rotation in Grad
     */
    public Transform(Vector2f position, Vector2f scale, float rotation) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
    }

    /**
     * Setzt einfach alles: Position auf (0, 0), Scale auf (1, 1) und Rotation auf 0.
     */
    public Transform() {
        this(new Vector2f(0, 0), new Vector2f(1, 1), 0);
    }

    /**
     * Addiert den übergebenen Vektor zur aktuellen Position.
     *
     * @param vec Vektor, der hinzuaddiert wird
     */
    public void move(Vector2f vec) {
        this.position.add(vec);
    }

    /**
     * Addiert die x- und y-Werte zur aktuellen Position.
     *
     * @param x Wert, der zur x-Position addiert wird
     * @param y Wert, der zur y-Position addiert wird
     */
    public void move(float x, float y) {
        this.position.add(x, y);
    }

    /**
     * Setzt die Position auf den neuen Vektor.
     *
     * @param vec Neue Position
     */
    public void setPosition(Vector2f vec) {
        this.position.set(vec);
    }

    /**
     * Setzt die Position auf die neuen x- und y-Werte.
     *
     * @param x Neue x-Position
     * @param y Neue y-Position
     */
    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    /**
     * Gibt die aktuelle Rotation in Grad zurück.
     *
     * @return Aktueller Rotationswinkel
     */
    public float GetRotation() {
        return this.rotation;
    }

    /**
     * Erhöht die Rotation um den übergebenen Winkel.
     *
     * @param angle Winkel, der zur Rotation addiert wird (in Grad)
     */
    public void rotate(float angle) {
        this.rotation += angle;
    }

    /**
     * Setzt die Rotation auf den angegebenen Winkel.
     *
     * @param angle Neuer Rotationswinkel (in Grad)
     */
    public void setRotation(float angle) {
        this.rotation = angle;
    }

    /**
     * Gibt eine Kopie der aktuellen Position zurück.
     *
     * @return Kopie der Position
     */
    public Vector2f GetPosition() {
        return new Vector2f(position);
    }

    /**
     * Gibt eine Kopie der aktuellen Scale zurück.
     *
     * @return Kopie der Skalierung
     */
    public Vector2f GetScale() {
        return new Vector2f(scale);
    }

    /**
     * Setzt die Scale auf den neuen Vektor.
     *
     * @param scale Neuer Scale-Vektor
     */
    public void setScale(Vector2f scale) {
        this.scale.set(scale);
    }

    /**
     * Setzt die Scale auf die neuen x- und y-Werte.
     *
     * @param x Neuer x-Scale-Wert
     * @param y Neuer y-Scale-Wert
     */
    public void setScale(float x, float y) {
        this.scale.set(x, y);
    }

    /**
     * Baut aus Position, Scale und Rotation eine Transform-Matrix.
     *
     * @return Die erstellte Transformationsmatrix
     */
    public Matrix4f GenerateTransformMatrix() {
        Matrix4f transform = new Matrix4f();
        transform.identity()
                 .translate(this.position.x, this.position.y, -10)
                 .scale(this.scale.x, this.scale.y, 1)
                 .rotateZ((float) Math.toRadians(rotation));
        return transform;
    }
}
