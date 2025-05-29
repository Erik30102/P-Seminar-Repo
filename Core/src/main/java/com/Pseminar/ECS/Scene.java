package com.Pseminar.ECS;

import java.util.List;
import java.util.Map;

import com.Pseminar.Assets.Asset;
import com.Pseminar.ECS.Component.ComponentType;

import java.util.ArrayList;
import java.util.HashMap;

public class Scene extends Asset{
    private List<Entity> entities = new ArrayList<>();  
    private Map<ComponentType, List<Component>> components = new HashMap<>();

	@SuppressWarnings("rawtypes")
    private transient Map<ComponentType, IEntityListener> listenerDictionary = new HashMap<>();

    private static int IdRegister = 0;

    /**
     * damit ersttellt man neue entites in die scene die dann returnd wird und bearbeitet werden kann
     * 
     * @return das ertstellte entity
     */
    public Entity CreateEntity() {
        Entity entity = new Entity();
        entity.SetScene(this);
        entities.add(entity);
        return entity;
    }
    /**
     * Jedes entity hat eine id die man mit e.getId() bekommt und hier kann man ein entity damit dann wieder in der scene finden
     * 
     * @param entityId die id des entties
     * @return
     */
    public Entity GetEntityById(int entityId) {
        return entities.get(entityId);
    }

    /**
     * Wenn man alle components eines types haben will dann geht das hiermit
     * 
     * @param type der type
     * @return
     */
    public List<Component> GetComponentsByType(ComponentType type) {
        return components.get(type);
    }

    /**
     * Nur public um vom entity benutzt zu werden eigentlich ned wichtig internall scheiß ned anfassen
     * 
     * @return
     */
    public static int CreateEntityId() {
        return IdRegister++;
    }

    /**
     * Internal method
     * wird benutzt damit in der ComponentType - ComponentArray Map das Component Hinzugefügt wird benutzt einfach die AddComponent Method
     * 
     * @param component
     */
    @SuppressWarnings("unchecked")
    public void RegisterComponentInTypeComponentMap(Component component) {
        if(!this.components.containsKey(component.GetComponentType())){
            this.components.put(component.GetComponentType(), new ArrayList<>());
        }

        if (listenerDictionary.containsKey(component.GetComponentType())) {
			listenerDictionary.get(component.GetComponentType()).OnEntityAdded(component.GetEntity(), component);
		}

        this.components.get(component.GetComponentType()).add(component);
    }

    /**
     * Glaub muss man ned erklären is ne liste vonallen entities
     * 
     * @return rate mal
     */
    public List<Entity> GetEntites() {
        return this.entities;
    }

    /**
     * Regisriert einen neune Listener des interfaces IEntityListener womit man auf component erstell und component delete events hören kann einer bestimmten component types. wird eigentlich bis jetzt nur benutzt um physik components bei der physik engine zu registrieren eigentlich auch engine code
     * 
     * @param <T> Glaub braucht man ned mal einfach ignoriren 
     * @param componentT Der Component type für den der listener funktionieren soll
     * @param listener Der Eigentliche Listener bei dem dann alle funktionen des interfaces IEntityListener automatisch ausgeführt werden
     */
    public <T extends Component> void AddListener(ComponentType componentT, IEntityListener<T> listener) {
		if(listenerDictionary == null) 
            this.listenerDictionary = new HashMap<>();
        
        listenerDictionary.put(componentT, listener);
	}

    /**
     * Removed dann den listener des interfaces IEntityListener wieder für type x
     * 
     * @param component der type für den der listener removed werden soll
     */
    public void RemoveListener(ComponentType component) {
		listenerDictionary.remove(component);
	}

    /**
     * wenn zuerst alle components hinzugefügt werden und dann die listener kann man das callen um dann alle listnerer abfragen nachzuholen. Wiedermal Internall engine stuff
     */
    @SuppressWarnings("unchecked")
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
