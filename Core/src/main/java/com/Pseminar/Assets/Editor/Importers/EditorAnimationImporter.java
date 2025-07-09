package com.Pseminar.Assets.Editor.Importers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.Pseminar.Assets.Asset;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Editor.IEditorAssetImporter;
import com.Pseminar.Assets.Editor.IntermidiateAssetData;
import com.Pseminar.Assets.Editor.Serializer.GsonEditorAnimationSerilaizer;
import com.Pseminar.Graphics.Animation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EditorAnimationImporter implements IEditorAssetImporter {

    @Override
    public Asset LoadAsset(IntermidiateAssetData assetMetaData) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Animation.class, new GsonEditorAnimationSerilaizer()).create();

        try {
            return gson.fromJson(Files.readString(Path.of(ProjectInfo.GetProjectInfo().GetProjectPath() + assetMetaData.GetPath())), Animation.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void SerializeAsset(String path, Asset asset) {
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Animation.class, new GsonEditorAnimationSerilaizer()).create();

        String json = gson.toJson(asset);
        try {
            Files.writeString(Path.of(path), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
