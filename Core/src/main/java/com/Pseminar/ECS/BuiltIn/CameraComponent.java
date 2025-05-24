package com.Pseminar.ECS.BuiltIn;

import com.Pseminar.ECS.Component;
import com.Pseminar.renderer.OrthographicCamera;

public class CameraComponent extends Component {
    private OrthographicCamera camera;
    private boolean isActive = true;

    public CameraComponent() {
        camera = new OrthographicCamera();
    }

    /**
     * setzte die camera als aktive es sollte am besten immer nur eine aktive sein wird aber auch nicht geprüuft es wird immer die erste in der list die aktiv ist bentut
     * 
     * @param active
     */
    public void SetActive(boolean active) {
        isActive = active;
    }

    /**
     * 
     * @return is camera aktive
     */
    public boolean GetActive() {
        return isActive;
    }

    /**
     * 
     * @return die internalle representation der camera
     */
    public OrthographicCamera GetCamera() {
        return camera;
    }

    @Override
    public ComponentType GetComponentType() {
        return ComponentType.CameraComponent;
    }
}
