package com.Pseminar.ECS.BuiltIn;

import com.Pseminar.ECS.Component;

public abstract class BaseComponent extends Component {
    @Override
    public final ComponentType GetComponentType() {
        return ComponentType.BaseComponent;
    }

    /**
     * wird jeden frame vor dem rendern gecalled
     * 
     * @param dt der zeit unterschied zwichen diesem und letzten frame
     */
    public void OnUpdate(float dt) {}

    /**
     * wird einmal am start gecalled
     */
    public void OnStart() {}

    /**
     * wird einmal vorm zerstören des components gecalled
     */
    public void OnDispose() {}
}
