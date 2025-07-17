package com.Pseminar.ECS.BuiltIn;

import com.Pseminar.ECS.Component;
import com.Pseminar.Physics.PhysicsBody;

// kann akutell nur boxen weil ich kein bock aktuell hab den rest hinzuzufügen
public class RidgedBodyComponent extends Component {
    private boolean RotationLocked = false;

    private float width = 0.5f;
    private float height = 0.5f;

    private transient PhysicsBody rg;

    /** 
     * @return ComponentType
     */
    @Override
    public ComponentType GetComponentType() {
        return ComponentType.RidgedBodyComponent;
    }

    /** 
     * @return boolean
     */
    public boolean IsRotationLocked() {
		return this.RotationLocked;
	}

	/** 
     * @param isRotationLocked
     */
    public void SetIsRotationLocked(boolean isRotationLocked) {
		this.RotationLocked = isRotationLocked;
	}
    
    /** 
     * @return float
     */
    public float GetWidth() {
        return width;
    }

    /** 
     * @param width
     */
    public void SetWidth(float width) {
        this.width = width;
    }

    /** 
     * @return float
     */
    public float GetHeight() {
        return height;
    }

    /** 
     * @param height
     */
    public void SetHeight(float height) {
        this.height = height;
    }

    /** 
     * @param rg
     */
    public void SetBody(PhysicsBody rg) {
        this.rg = rg;
    }

    /** 
     * @return PhysicsBody
     */
    // TEMPORARY
    public PhysicsBody GetBody() {
        return this.rg;
    }
}
