package com.Pseminar.Assets.Editor.Serializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import javax.script.ScriptEngine;

import com.Pseminar.Logger;
import com.Pseminar.Assets.Asset;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.ScriptingEngine;
import com.Pseminar.ECS.Component;
import com.Pseminar.ECS.Entity;
import com.Pseminar.ECS.Scene;
import com.Pseminar.ECS.BuiltIn.AnimationSpriteComponent;
import com.Pseminar.ECS.BuiltIn.BaseComponent;
import com.Pseminar.ECS.BuiltIn.CameraComponent;
import com.Pseminar.ECS.BuiltIn.RidgedBodyComponent;
import com.Pseminar.ECS.BuiltIn.SpriteComponent;
import com.Pseminar.ECS.BuiltIn.TilemapComponent;
import com.Pseminar.Graphics.Sprite;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonEditorSceneSerializer implements JsonDeserializer<Scene>, JsonSerializer<Scene> {

    /** 
     * @param src
     * @param typeOfSrc
     * @param context
     * @return JsonElement
     */
    @Override
    public JsonElement serialize(Scene src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        JsonArray entities = new JsonArray();

        for(Entity e : src.GetEntites()) {
            JsonObject entityObject = new JsonObject();
            entityObject.addProperty("id", e.getId());
            entityObject.addProperty("name", e.GetName());
            entityObject.add("transfrom", context.serialize(e.transform));

            JsonArray componentArray = new JsonArray();
            for(Component c : e.getComponents()) {
                JsonObject componentObject = new JsonObject();
                componentObject.addProperty("type", c.GetComponentType().toString());
                componentObject.addProperty("id", c.GetComponentId());

                switch (c.GetComponentType()) {
                    case CameraComponent:
                        componentObject.addProperty("active", ((CameraComponent)c).GetActive());
                        componentObject.addProperty("zoom", ((CameraComponent)c).GetCamera().GetZoom());
                        break;
                    case SpriteComponent:
                        componentObject.addProperty("spriteId", ((SpriteComponent)c).GetSprite().GetAssetId());
                        break;
                    case AnimationComponent:
                        componentObject.addProperty("animationId", ((AnimationSpriteComponent)c).GetAnimation().GetAssetId());
                        break;
                    case TilemapComponent:
                        componentObject.addProperty("tilemapId", ((TilemapComponent)c).GetTilemap().GetAssetId());
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

                componentArray.add(componentObject);
            }

            entityObject.add("components", componentArray);
            entities.add(entityObject);
        }

        result.add("entites", entities);
        return result;
    }

    /** 
     * @param componentObject
     * @param c
     * @param context
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
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

                if(Asset.class.isAssignableFrom(type)) {
                    componentObject.addProperty(name, ((Asset) value).GetAssetId());
                } else {
                    componentObject.add(name, context.serialize(value));
                }
            }
        }
    }
                    
     /** 
      * @param json
      * @param typeOfT
      * @param context
      * @return Scene
      * @throws JsonParseException
      */
     @Override
    public Scene deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray entitiesJson = jsonObject.getAsJsonArray("entites");

        Scene scene = new Scene();

        for (JsonElement entityElem : entitiesJson) {
            JsonObject entityJson = entityElem.getAsJsonObject();

            int id = entityJson.get("id").getAsInt();
            Entity entity = scene.CreateEntity(); // TODO: implement id but scene is written so fucking awfully that i cant be botherd

            entity.SetName(entityJson.get("name").getAsString());
            entity.transform = context.deserialize(entityJson.get("transfrom"), entity.transform.getClass());

            JsonArray componentsJson = entityJson.getAsJsonArray("components");
            for (JsonElement compElem : componentsJson) {
                JsonObject compJson = compElem.getAsJsonObject();

                String type = compJson.get("type").getAsString();
                Component comp = null;

                switch (type) {
                    case "CameraComponent":
                        CameraComponent cam = new CameraComponent();
                        cam.SetActive(compJson.get("active").getAsBoolean());
                        cam.GetCamera().SetZoom(compJson.get("zoom").getAsFloat());
                        comp = cam;
                        break;
                    case "SpriteComponent":
                        int spriteId = compJson.get("spriteId").getAsInt();
                        Sprite asset = ProjectInfo.GetProjectInfo().GetAssetManager().GetAsset(spriteId);
                        SpriteComponent sprite = new SpriteComponent();
                        sprite.SetSprite(asset);
                        comp = sprite;
                        break;
                    case "RidgedBodyComponent":
                        comp = new RidgedBodyComponent();
                        break;
                    case "AnimationComponent": 
                        int animId = compJson.get("animationId").getAsInt();
                        comp = new AnimationSpriteComponent();
                        ((AnimationSpriteComponent)comp).SetAnimation(ProjectInfo.GetProjectInfo().GetAssetManager().GetAsset(animId));
                        break;
                    case "TilemapComponent":
                        int tilemapId = compJson.get("tilemapId").getAsInt();
                        comp = new TilemapComponent();
                        ((TilemapComponent)comp).SetTilemap(ProjectInfo.GetProjectInfo().GetAssetManager().GetAsset(tilemapId));
                        default:
                        break;
                }

                if (comp == null && compJson.has("className")) {
                    String className = compJson.get("className").getAsString();
                    try {
                        Class<?> clazz = ScriptingEngine.GetInstance().GetBaseClass(className);
                        Object instance = clazz.getDeclaredConstructor().newInstance();
                        if (instance instanceof BaseComponent) {
                            BaseComponent baseComp = (BaseComponent) instance;

                            for (Field field : clazz.getDeclaredFields()) {
                                if (!Modifier.isPrivate(field.getModifiers()) &&
                                    !Modifier.isStatic(field.getModifiers()) &&
                                    !Modifier.isTransient(field.getModifiers())) {

                                    field.setAccessible(true);
                                    String fieldName = field.getName();
                                    JsonElement fieldJson = compJson.get(fieldName);

                                    if (Asset.class.isAssignableFrom(field.getType())) {
                                        Integer assetId = fieldJson.getAsInt();
                                        Asset assetField = ProjectInfo.GetProjectInfo().GetAssetManager().GetAsset(assetId); // implement this
                                        field.set(baseComp, assetField);
                                    } else {
                                        Object fieldValue = context.deserialize(fieldJson, field.getGenericType());
                                        field.set(baseComp, fieldValue);
                                    }
                                }
                            }

                            comp = baseComp;
                        }
                    } catch (Exception e) {
                        Logger.error("Failed to deserialize BaseComponent: " + className);
                        e.printStackTrace();
                    }
                }

                if (comp != null) {
                    entity.AddComponent(comp);
                }
            }
        }

        return scene;
    }
}

