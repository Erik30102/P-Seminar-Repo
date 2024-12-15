package com.Pseminar.ECS;

import java.util.List;
import java.util.Map;

import com.Pseminar.ECS.Component.ComponentType;

import java.util.ArrayList;
import java.util.HashMap;

public class Scene {
    private List<Entity> entities;  
    private Map<ComponentType, List<Component>> components;

    private static int IdRegister = 0;

    public Scene() {
        this.entities = new ArrayList<>();
        this.components = new HashMap<>();
    }

    public void AddEntity(Entity entity) {
        entity.SetScene(this);
        entities.add(entity);
    }

    public Entity CreateEntity() {
        Entity entity = new Entity();
        entity.SetScene(this);
        entities.add(entity);
        return entity;
    }

    public void ListComponentsOfEntity(int entityId) {
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

    public Entity GetEntityById(int entityId) {
        return entities.get(entityId);
    }

    public List<Component> GetComponentsByType(ComponentType type) {
        return components.get(type);
    }

    public void ListComponentsByType(Class<? extends Component> componentType) {
        System.out.println("Listing all components of type " + componentType.getSimpleName() + ":");
        for (Entity entity : entities) {
            for (Component component : entity.getComponents()) {
                if (componentType.isInstance(component)) {
                    System.out.println("Entity " + entity.getId() + " has " + component.toString());
                }
            }
        }
    }

    public static int CreateEntityId() {
        return IdRegister++;
    }

    /**
     * Internal method
     * wird benutzt damit in der ComponentType - ComponentArray Map das Component Hinzugefügt wird benutzt einfach die AddComponent Method
     * 
     * @param component
     */
    public void RegisterComponentInTypeComponentMap(Component component) {
        if(!this.components.containsKey(component.GetComponentType())){
            this.components.put(component.GetComponentType(), new ArrayList<>());
        }

        this.components.get(component.GetComponentType()).add(component);
    }
}
