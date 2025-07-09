package com.Pseminar.Assets.Editor.Importers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.Pseminar.Assets.Asset;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Editor.IEditorAssetImporter;
import com.Pseminar.Assets.Editor.IntermidiateAssetData;
import com.Pseminar.Assets.Editor.Serializer.GsonSpriteSheetSerilailizer;
import com.Pseminar.Graphics.SpriteSheet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EditorSpriteSheetImporter implements IEditorAssetImporter {

    @Override
    public Asset LoadAsset(IntermidiateAssetData assetMetaData) {
        Gson gson = new GsonBuilder().registerTypeAdapter(SpriteSheet.class, new GsonSpriteSheetSerilailizer()).create();

        try {
            return gson.fromJson(Files.readString(Path.of(ProjectInfo.GetProjectInfo().GetProjectPath() + assetMetaData.GetPath())), SpriteSheet.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void SerializeAsset(String path, Asset asset) {
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(SpriteSheet.class, new GsonSpriteSheetSerilailizer()).create();

        String json = gson.toJson(asset);
        try {
            Files.writeString(Path.of(path), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
