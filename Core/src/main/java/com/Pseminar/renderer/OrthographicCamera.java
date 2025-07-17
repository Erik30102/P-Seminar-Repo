package com.Pseminar.renderer;

import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.Pseminar.Logger;

public class OrthographicCamera {
    private transient Matrix4f transformMatrix;
    private transient Matrix4f projectionMatrix;

    private transient Matrix4f inversTransformMatrix;
    private transient Matrix4f inversProjectionMatrix;

    private float Zoom = 10;
    private float currentAspectRatio = 1;

    private Vector2f position = new Vector2f(0,0);

    public OrthographicCamera() {
        projectionMatrix = new Matrix4f();
        transformMatrix = new Matrix4f();

        this.ReCalculateProjectionMatrix();
        this.ReCalculateTransformMatrix();
    }
        
    /** 
     * @param width
     * @param height
     */
    public void Resize(float width, float height) {
        currentAspectRatio = width / height;

        this.ReCalculateProjectionMatrix();
    }
    
    private void ReCalculateProjectionMatrix() {
        float width = Zoom * 0.5f * currentAspectRatio;
		float height = Zoom * 0.5f;

		projectionMatrix.identity();
		projectionMatrix.ortho(-width, width, -height, height, 0f, 100f);

        inversProjectionMatrix = projectionMatrix.invert(new Matrix4f());
    }
    
    private void ReCalculateTransformMatrix() {
        Vector3f cameraFront = new Vector3f(0, 0, -1);
		Vector3f cameraUp = new Vector3f(0, 1, 0);
		transformMatrix.identity();
		transformMatrix = transformMatrix.lookAt(
				new Vector3f(
						position.x(),
						position.y(), 20),
				cameraFront.add(position.x(), position.y(), 0), cameraUp);

        inversTransformMatrix = transformMatrix.invert(new Matrix4f());
    }

    /** 
     * @param cameraPos
     */
    public void Move(Vector2f cameraPos) {
        this.position = cameraPos;
        ReCalculateTransformMatrix();
    }

    /** 
     * @param newZoom
     */
    public void SetZoom(float newZoom) {
        this.Zoom = newZoom;    
    }  

    /** 
     * @return Matrix4f
     */
    public Matrix4f GetProjectionMatrix() {
        return this.projectionMatrix;
    }

    /** 
     * @return Matrix4f
     */
    public Matrix4f GetTransformMatrix() {
        return this.transformMatrix;
    }

    /** 
     * @return Matrix4f
     */
    public Matrix4f GetInversTransformationMatrix() {
        return this.inversTransformMatrix;
    }

    /** 
     * @return Matrix4f
     */
    public Matrix4f GetInversProjectionMatrix() {
        return this.inversProjectionMatrix;
    }

    /**
     * DO NOT USE JUST FOR SCNEE CAMERA VERY SHIT
     * 
     * @param detla
     */
    public void MoveBy(Vector2d detla) {
        this.Move(position.add((float)detla.x() * 0.02f, (float)detla.y() * -0.02f));
    }

    /** 
     * @return float
     */
    public float GetZoom() {
        return this.Zoom;
    }
}
