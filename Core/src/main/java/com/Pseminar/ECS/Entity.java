package com.Pseminar.ECS;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;

/**
*Klasse Entity ist Container für verschiedene Components
*welche ihr eigenschaften und Verhaltensweisen verleihen
*(z.B.kann ein Component ein PositionsComponent sein, was die Position definiert)
*/
public class Entity implements Serializable{
    private int id;
    private List<Component> components;
    private String name;

    private Scene scene;//Szene zu der die Entity gehört
    public Transform transform;

    /**
    *Konstruktor erstellt eine neue Entity mit einer ID
    *leere Liste der Components wird initialisiert
    */
    public Entity() {
        this.id = Scene.CreateEntityId();
        this.transform = new Transform();
        this.components = new ArrayList<>();

        this.name = "Entity " + this.id;
    }
    
    //legt fest zu welcher Szene die Entity angehört
    public final void SetScene(Scene scene){
        this.scene = scene;
    }

    public void SetName(String name) {
        this.name = name;
    }

    public String GetName(){
        return this.name;
    }

    public int getId() {
        return id;
    }

	public <T extends Component> T GetComponent(Class<T> clazz) {
        for (Component component : this.components) {
            if(component.getClass().isAssignableFrom(clazz)) {
                return (T)component;
            }
        }
        
        return null;
    }

    public void AddComponent(Component component) {
        component.setEntity(this);
        components.add(component);

        this.scene.RegisterComponentInTypeComponentMap(component);
    }

    public List<Component> getComponents() {
        return components;
    }

}
