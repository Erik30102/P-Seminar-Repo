package com.Pseminar.ECS.BuiltIn;

import com.Pseminar.ECS.Component;
import com.Pseminar.Physics.PhysicsBody;

// kann akutell nur boxen weil ich kein bock aktuell hab den rest hinzuzufügen
public class RidgedBodyComponent extends Component {
    private boolean RotationLocked = false;

    private float width = 0.5f;
    private float height = 0.5f;

    private transient PhysicsBody rg;

    @Override
    public ComponentType GetComponentType() {
        return ComponentType.RidgedBodyComponent;
    }

    public boolean IsRotationLocked() {
		return this.RotationLocked;
	}

	public void SetIsRotationLocked(boolean isRotationLocked) {
		this.RotationLocked = isRotationLocked;
	}
    
    public float GetWidth() {
        return width;
    }

    public void SetWidth(float width) {
        this.width = width;
    }

    public float GetHeight() {
        return height;
    }

    public void SetHeight(float height) {
        this.height = height;
    }

    public void SetBody(PhysicsBody rg) {
        this.rg = rg;
    }

    // TEMPORARY
    public PhysicsBody GetBody() {
        return this.rg;
    }
}
