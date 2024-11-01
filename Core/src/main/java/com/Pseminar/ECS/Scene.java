package com.Pseminar.ECS;

import java.util.List;
import java.util.ArrayList;

public class Scene {
    private List<Entity> entities;

    public Scene() {
        this.entities = new ArrayList<>();
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void listEntities() {
        for (Entity entity : entities) {
            System.out.println("Entity ID: " + entity.getId());
            for (Component component : entity.getComponents()) {
                System.out.println(" - " + component.toString());
            }
        }
    }

    public void listComponentsOfEntity(int entityId) {
        for (Entity entity : entities) {
            if (entity.getId() == entityId) {
                System.out.println("Components of Entity " + entityId + ":");
                for (Component component : entity.getComponents()) {
                    System.out.println(" - " + component.toString());
                }
                return;
            }
        }
        System.out.println("Entity with ID " + entityId + " not found.");
    }

    public void listComponentsByType(Class<? extends Component> componentType) {
        System.out.println("Listing all components of type " + componentType.getSimpleName() + ":");
        for (Entity entity : entities) {
            for (Component component : entity.getComponents()) {
                if (componentType.isInstance(component)) {
                    System.out.println("Entity " + entity.getId() + " has " + component.toString());
                }
            }
        }
    }

}
