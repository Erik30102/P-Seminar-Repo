package com.Pseminar.Assets.Editor.Serializer;

import java.lang.reflect.Type;

import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Graphics.Animation;
import com.Pseminar.Graphics.SpriteSheet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonEditorAnimationSerilaizer implements JsonDeserializer<Animation>, JsonSerializer<Animation> {

    @Override
    public JsonElement serialize(Animation src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("spritesheetId", src.GetSpritesheet().GetAssetId());
        jsonObj.add("keyframes", context.serialize(src.GetAnimationKeyFrames()));
        jsonObj.addProperty("frameDuration", src.GetFrameDuration());

        return jsonObj;
    }

    @Override
    public Animation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        SpriteSheet s = ProjectInfo.GetProjectInfo().GetAssetManager().GetAsset(json.getAsJsonObject().get("spritesheetId").getAsInt());
        int[] k = context.deserialize(json.getAsJsonObject().get("keyframes"), int[].class);

        Animation a = new Animation(s, k);
        a.SetFrameDruation(json.getAsJsonObject().get("frameDuration").getAsFloat());
        
        return a;
    }
    
}
