package com.Pseminar.ECS.BuiltIn;

import java.io.IOException;

import com.Pseminar.ECS.Component;
import com.Pseminar.renderer.OrthographicCamera;

public class CameraComponent extends Component {
    private transient OrthographicCamera camera;
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

    /** 
     * @return ComponentType
     */
    @Override
    public ComponentType GetComponentType() {
        return ComponentType.CameraComponent;
    }

    /** 
     * @param stream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(java.io.ObjectInputStream stream)
    throws IOException, ClassNotFoundException {
        this.isActive = stream.readBoolean();

        this.camera = new OrthographicCamera();
    }

    /** 
     * @param stream
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeBoolean(isActive);
    }
}
