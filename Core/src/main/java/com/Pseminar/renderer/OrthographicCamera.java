package com.Pseminar.renderer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class OrthographicCamera {
    private Matrix4f transformMatrix;
    private Matrix4f projectionMatrix;

    private float Zoom = 10;
    private float currentAspectRatio = 1;

    private Vector2f position = new Vector2f(0,0);

    public OrthographicCamera() {
        projectionMatrix = new Matrix4f();
        transformMatrix = new Matrix4f();

        this.ReCalculateProjectionMatrix();
        this.ReCalculateTransformMatrix();
    }
        
    public void Resize(float width, float height) {
        currentAspectRatio = width / height;

        this.ReCalculateProjectionMatrix();
    }
    
    private void ReCalculateProjectionMatrix() {
        float width = Zoom * 0.5f * currentAspectRatio;
		float height = Zoom * 0.5f;

		projectionMatrix.identity();
		projectionMatrix.ortho(-width, width, -height, height, 0f, 100f);
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
    }

    public void SetZoom(float newZoom) {
        this.Zoom = newZoom;    
    }  

    public Matrix4f GetProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Matrix4f GetTransformMatrix() {
        return this.transformMatrix;
    }
}
