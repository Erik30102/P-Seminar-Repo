package com.Pseminar.Assets.Editor.Importers;

import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.Pseminar.Assets.Asset;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Editor.IEditorAssetImporter;
import com.Pseminar.Assets.Editor.IntermidiateAssetData;
import com.Pseminar.Assets.Editor.Serializer.GsonEditorSceneSerializer;
import com.Pseminar.ECS.Scene;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EditorSceneImporter implements IEditorAssetImporter {

    @Override
    public Asset LoadAsset(IntermidiateAssetData assetMetaData) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Scene.class, new GsonEditorSceneSerializer()).create();

        try {
            return gson.fromJson(Files.readString(Path.of(ProjectInfo.GetProjectInfo().GetProjectPath() + assetMetaData.GetPath())), Scene.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void SerializeAsset(String path, Asset asset) {
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Scene.class, new GsonEditorSceneSerializer()).create();

        String json = gson.toJson(asset);
        try {
            Files.writeString(Path.of(path), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
