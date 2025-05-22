package com.Pseminar.ECS;

import java.util.List;
import java.util.Map;

import com.Pseminar.Assets.Asset;
import com.Pseminar.ECS.Component.ComponentType;

import java.util.ArrayList;
import java.util.HashMap;

public class Scene  extends Asset{
    private List<Entity> entities;  
    private Map<ComponentType, List<Component>> components;

	@SuppressWarnings("rawtypes")
    private transient Map<ComponentType, IEntityListener> listenerDictionary;

    private static int IdRegister = 0;

    public Scene() {
        this.entities = new ArrayList<>();
        this.components = new HashMap<>();
        this.listenerDictionary = new HashMap<>();
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

        if (listenerDictionary.containsKey(component.GetComponentType())) {
			listenerDictionary.get(component.GetComponentType()).OnEntityAdded(component.GetEntity(), component);
		}

        this.components.get(component.GetComponentType()).add(component);
    }

    public List<Entity> GetEntites() {
        return this.entities;
    }

    public <T extends Component> void AddListener(ComponentType componentT, IEntityListener<T> listener) {
		listenerDictionary.put(componentT, listener);
	}

    public void RemoveListener(ComponentType component) {
		listenerDictionary.remove(component);
	}

    public void RunAllAddingListeners() {
        for(List<Component> _components : this.components.values()) {
            for(Component component : _components) {
                if (listenerDictionary.containsKey(component.GetComponentType())) {
                    listenerDictionary.get(component.GetComponentType()).OnEntityAdded(component.GetEntity(), component);
                }
            }
        }
    }

    @Override
    public AssetType GetAssetType() {
        return AssetType.SCENE;
    }

    @Override
    public void OnDispose() {
        // TODO
    }
}
