package com.Pseminar.ECS.BuiltIn;

import com.Pseminar.ECS.Component;

public abstract class BaseComponent extends Component {
    @Override
    public final ComponentType GetComponentType() {
        return ComponentType.BaseComponent;
    }

    public void OnUpdate(float dt) {}
    public void OnStart() {}
    public void OnDispose() {}
}
