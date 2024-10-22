package com.Scene;

import java.util.ArrayList;
import java.util.List;

interface Component {
    // Marker Interface für Komponenten
}

class PositionComponent implements Component {
    public float x, y;

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "PositionComponent[x=" + x + ", y=" + y + "]";
    }
}

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

class Entity {
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

class Scene {
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

// Main
public class Welt {
    public static void main(String[] args) {
        Scene scene = new Scene();

        Entity entity1 = new Entity(1);
        entity1.addComponent(new PositionComponent(10, 20));
        entity1.addComponent(new RenderComponent("player_sprite"));

        Entity entity2 = new Entity(2);
        entity2.addComponent(new PositionComponent(15, 30));

        scene.addEntity(entity1);
        scene.addEntity(entity2);

        scene.listEntities();

        scene.listComponentsOfEntity(1);

        scene.listComponentsByType(PositionComponent.class);
    }
}
