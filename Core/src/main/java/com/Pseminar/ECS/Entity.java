package com.Pseminar.ECS;

import java.util.List;
import java.util.ArrayList;

public class Entity {
    private int id;
    private List<Component> components;

    private Scene scene;

    public Entity(int id) {
        this.id = id;
        this.components = new ArrayList<>();
    }

    public final void SetScene(Scene scene){
        this.scene = scene;
    }

    public int getId() {
        return id;
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public List<Component> getComponents() {
        return components;
    }

}
