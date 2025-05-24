package com.Pseminar.ECS;

public abstract class Component {
    public enum ComponentType {
        SpriteComponent, 
        BaseComponent,
        CameraComponent,

        RidgedBodyComponent,
    }

    private transient Entity entity;
    private int id;

    public final void setId(int id) {
        this.id = id;
    }

    public final void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity GetEntity() {
        return entity;
    }

    public int GetComponentId() {
        return this.id;
    }

    public abstract ComponentType GetComponentType();
} 
