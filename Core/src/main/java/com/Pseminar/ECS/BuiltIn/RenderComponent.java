package com.Pseminar.ECS.BuiltIn;

import com.Pseminar.ECS.Component;

class RenderComponent implements Component {
    public String sprite;

    public RenderComponent(String sprite) {
        this.sprite = sprite;
    }

    @Override
    public String toString() {
        return "RenderComponent[sprite=" + sprite + "]";
    }
}
