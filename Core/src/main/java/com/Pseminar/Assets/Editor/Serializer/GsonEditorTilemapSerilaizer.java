package com.Pseminar.Assets.Editor.Serializer;

import java.lang.reflect.Type;

import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.BuiltIn.Tilemap;
import com.Pseminar.Graphics.SpriteSheet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonEditorTilemapSerilaizer implements JsonDeserializer<Tilemap>, JsonSerializer<Tilemap> {

    @Override
    public JsonElement serialize(Tilemap src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("spriteSheetId", src.GetSpritesheet().GetAssetId());
        obj.addProperty("width", src.GetWidth());
        obj.addProperty("height", src.GetHeight());

        obj.add("tiles", context.serialize(src.GetTiles()));
        obj.add("Collding", context.serialize(src.GetCollidingMap()));

        return obj;
    }

    @Override
    public Tilemap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        int width = obj.get("width").getAsInt();
        int height = obj.get("height").getAsInt();

        int[] tiles = context.deserialize(obj.get("tiles"), int[].class);
        boolean[] isColliding = context.deserialize(obj.get("Collding"), boolean[].class);

        int assetId = obj.get("spriteSheetId").getAsInt();
        SpriteSheet a = ProjectInfo.GetProjectInfo().GetAssetManager().GetAsset(assetId);

        if(isColliding == null) {
            isColliding = new boolean[width * height];
        }

        return new Tilemap(a, width, height, tiles, isColliding);
    }
}
