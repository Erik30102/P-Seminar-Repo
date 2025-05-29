package com.Pseminar.ECS;

import java.io.Serializable;

public abstract class Component implements Serializable {
    public enum ComponentType {
        SpriteComponent, 
        BaseComponent,
        CameraComponent,

        RidgedBodyComponent,
    }

    private Entity entity;
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
