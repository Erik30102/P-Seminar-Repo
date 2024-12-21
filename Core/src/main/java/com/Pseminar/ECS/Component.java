package com.Pseminar.ECS;

public abstract class Component {
    public enum ComponentType {
        SpriteComponent, 
        BaseComponent,
        CameraComponent
    }

    private Entity entity;

    public final void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity GetEntity() {
        return entity;
    }

    public abstract ComponentType GetComponentType();
} 
