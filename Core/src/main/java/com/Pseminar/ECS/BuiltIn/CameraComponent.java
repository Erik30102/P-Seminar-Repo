package com.Pseminar.ECS.BuiltIn;

import com.Pseminar.ECS.Component;
import com.Pseminar.renderer.OrthographicCamera;

public class CameraComponent extends Component {
    private OrthographicCamera camera;
    private boolean isActive = true;

    public CameraComponent() {
        camera = new OrthographicCamera();
    }

    public void SetActive(boolean active) {
        isActive = active;
    }

    public boolean GetActive() {
        return isActive;
    }

    public OrthographicCamera GetCamera() {
        return camera;
    }

    @Override
    public ComponentType GetComponentType() {
        return ComponentType.CameraComponent;
    }
}
