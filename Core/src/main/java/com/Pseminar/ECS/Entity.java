package com.Pseminar.ECS;

import java.util.List;

import org.joml.Vector2f;

import com.Pseminar.ECS.BuiltIn.RidgedBodyComponent;

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
    
    /** 
     * legt fest zu welcher Szene die Entity angehört
     * @param scene
     */
    public final void SetScene(Scene scene){
        this.scene = scene;
    }

    /** 
     * @param name
     */
    public void SetName(String name) {
        this.name = name;
    }

    /** 
     * @return String
     */
    public String GetName(){
        return this.name;
    }

    /** 
     * @return int
     */
    public int getId() {
        return id;
    }

    /** 
     * @return Scene
     */
    public Scene GetScene() {
        return this.scene;
    }

	/** 
     * @param clazz
     * @return T
     */
    public <T extends Component> T GetComponent(Class<T> clazz) {
        for (Component component : this.components) {
            if(component.getClass().isAssignableFrom(clazz)) {
                return (T)component;
            }
        }
        
        return null;
    }

    /** 
     * @param component
     */
    public void AddComponent(Component component) {
        component.setEntity(this);
        components.add(component);

        this.scene.RegisterComponentInTypeComponentMap(component);
    }

    /** 
     * @return List<Component>
     */
    public List<Component> getComponents() {
        return components;
    }

    /** 
     * @param position
     */
    public void ForceSetPosition(Vector2f position) {
        if(GetComponent(RidgedBodyComponent.class) != null)
            GetComponent(RidgedBodyComponent.class).GetBody().SetPosition(position);

        this.transform.setPosition(position);
    }

}
