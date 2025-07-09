package com.Pseminar.Assets.Editor.Serializer;

import java.lang.reflect.Type;

import com.Pseminar.Graphics.Sprite;
import com.Pseminar.Graphics.SpriteSheet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonSpriteSheetSerilailizer implements JsonDeserializer<SpriteSheet>, JsonSerializer<SpriteSheet> {

    @Override
    public JsonElement serialize(SpriteSheet src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("SpriteWidth", src.getSpriteWidth());
        obj.addProperty("SpriteHeight", src.getSpriteHeight());
    
        obj.addProperty("texId", src.getTexture().GetAssetId());

        return obj;
    }

    @Override
    public SpriteSheet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deserialize'");
    }
    
}
