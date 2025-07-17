package com.Pseminar.Assets.Editor.Serializer;

import java.lang.reflect.Type;

import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Graphics.Sprite;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonEditorSpriteSerializer implements JsonDeserializer<Sprite>, JsonSerializer<Sprite> {

    /** 
     * @param src
     * @param typeOfSrc
     * @param context
     * @return JsonElement
     */
    @Override
    public JsonElement serialize(Sprite src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.addProperty("textureId", src.getTexture().GetAssetId());
        result.add("uvs", context.serialize(src.getUv()));

        return result;
    }

    /** 
     * @param json
     * @param typeOfT
     * @param context
     * @return Sprite
     * @throws JsonParseException
     */
    @Override
    public Sprite deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        int assetId = json.getAsJsonObject().get("textureId").getAsInt();
        float[] uvs = context.deserialize(json.getAsJsonObject().get("uvs"), float[].class);

        return new Sprite(ProjectInfo.GetProjectInfo().GetAssetManager().GetAsset(assetId), uvs);
    }
    
}
