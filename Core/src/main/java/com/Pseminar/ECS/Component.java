package com.Pseminar.ECS;

import java.io.Serializable;


public abstract class Component implements Serializable {
    public enum ComponentType {
        SpriteComponent, 
        BaseComponent,
        CameraComponent,
        AnimationComponent,
        TilemapComponent,

        RidgedBodyComponent,
    }

    private Entity entity;
    private int id;

    /** 
     * @param id
     */
    public final void setId(int id) {
        this.id = id;
    }

    /** 
     * @param entity
     */
    public final void setEntity(Entity entity) {
        this.entity = entity;
    }

    /** 
     * @return Entity
     */
    public Entity GetEntity() {
        return entity;
    }

    /** 
     * @return int
     */
    public int GetComponentId() {
        return this.id;
    }

    public abstract ComponentType GetComponentType();
} 
