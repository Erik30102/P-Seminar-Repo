package com.Pseminar.Assets.Editor.Serializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import com.Pseminar.Logger;
import com.Pseminar.Assets.Asset;
import com.Pseminar.ECS.Component;
import com.Pseminar.ECS.Entity;
import com.Pseminar.ECS.Scene;
import com.Pseminar.ECS.BuiltIn.BaseComponent;
import com.Pseminar.ECS.BuiltIn.CameraComponent;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonEditorSceneSerializer implements JsonDeserializer<Scene>, JsonSerializer<Scene> {

    @Override
    public JsonElement serialize(Scene src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject result = new JsonObject();
        JsonArray entities = new JsonArray();

        for(Entity e : src.GetEntites()) {
            JsonObject entityObject = new JsonObject();
            entityObject.addProperty("id", e.getId());
            entityObject.add("transfrom", context.serialize(e.transform));

            JsonArray componentArray = new JsonArray();
            for(Component c : e.getComponents()) {
                JsonObject componentObject = new JsonObject();
                componentObject.addProperty("type", c.GetComponentType().toString());

                switch (c.GetComponentType()) {
                    case CameraComponent:
                        componentObject.addProperty("active", ((CameraComponent)c).GetActive());
                        break;
                    case SpriteComponent:
                        // componentObject.addProperty("active", ((SpriteComponent)c).GetSprite().) // Sprites need to be assets for this to work
                    default:
                        break;
                }

                if(c instanceof BaseComponent) {
                    try {
                        this.SerizalizeBaseComponent(componentObject, (BaseComponent)c, context);
                    } catch (IllegalArgumentException | IllegalAccessException e1) {
                        Logger.error("Error while trying to SerizalizeBaseComponent of type " + c.getClass().getName());
                        e1.printStackTrace();
                    }
                }
            }

            entityObject.add("components", componentArray);
            entities.add(entityObject);
        }

        result.add("entites", entities);
        return result;
    }

    @SuppressWarnings("unchecked")
    private void SerizalizeBaseComponent(JsonObject componentObject, BaseComponent c, JsonSerializationContext context) throws IllegalArgumentException, IllegalAccessException {
        componentObject.addProperty("className", c.getClass().getName());
        
        Field[] fields = c.getClass().getDeclaredFields();
        for(Field field : fields) {
            if( !Modifier.isPrivate(field.getModifiers()) && 
                !Modifier.isStatic(field.getModifiers()) && 
                !Modifier.isTransient(field.getModifiers())) {

                @SuppressWarnings("rawtypes")
                Class type = field.getType();
                Object value = field.get(c);
                String name = field.getName();

                if(type.isAssignableFrom(Entity.class)) {
                    componentObject.addProperty(name, ((Entity) value).getId());
                }else if(type.isAssignableFrom(Component.class)) {
                    componentObject.addProperty(name, ((Component) value).GetComponentId());
                } else if(type.isAssignableFrom(Asset.class)) {
                    componentObject.addProperty(name, ((Asset) value).GetAssetId());
                } else {
                    componentObject.add(name, context.serialize(value));
                }
            }
        }
    }
                    
     @Override
    public Scene deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        throw new UnsupportedOperationException("Unimplemented method 'deserialize'");
    }
    
}

