package com.Pseminar.ECS;

import java.util.List;
import java.util.ArrayList;

public class Entity {
    private int id;
    private List<Component> components;

    public Entity(int id) {
        this.id = id;
        this.components = new ArrayList<>();
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
