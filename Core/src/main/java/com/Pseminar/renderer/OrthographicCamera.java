package com.Pseminar.renderer;

import org.joml.Matrix4f;

public class OrthographicCamera {
    private Matrix4f transformMatrix;
    private Matrix4f projectionMatrix;

    private float Zoom = 10;
    private float currentAspectRatio = 1;

    public OrthographicCamera() {
        projectionMatrix = new Matrix4f();
        transformMatrix = new Matrix4f();

        this.ReCalculateProjectionMatrix();
        transformMatrix.identity();
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
        // TODO: movement in camera
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
