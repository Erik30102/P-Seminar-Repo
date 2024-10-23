package com.Pseminar.Assets.Editor;

import java.util.HashMap;
import java.util.Map;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.Pseminar.Logger;
import com.Pseminar.Assets.Asset;
import com.Pseminar.Assets.AssetManager;
import com.Pseminar.Assets.Asset.AssetType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EditorAssetManager implements AssetManager {

    private transient Map<Integer, Asset> loadedAssets = new HashMap<>();
    private transient Map<AssetType,IEditorAssetImporter> AssetImporters = new HashMap<>(); 
    
    private Map<Integer, IntermidiateAssetData> assetMap = new HashMap<>();


    public EditorAssetManager() {
        this.SetupAssetImporters();
    }

    private void SetupAssetImporters() {

    }

    private Asset LoadAsset(IntermidiateAssetData assetMetaData) {
        if(!AssetImporters.containsKey(assetMetaData.GetAssetType())) {
            Logger.error("No importer for type: " + assetMetaData.GetAssetType());
            
            return null;
        }

        Asset loadedAsset = AssetImporters.get(assetMetaData.GetAssetType()).LoadAsset(assetMetaData);
        if(loadedAsset != null){ 
            Logger.error("Can not load Asset at location: " + assetMetaData.GetPath());
        }

        return null;
    }  

    @SuppressWarnings("unchecked")
    public void LoadAssetMap(String path) {
        try {
            String jsonAssetMap = Files.readString(Path.of(path));
            Gson gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create();

            // Glaub des geht garned aber muss ich noch ausprobieren
            this.assetMap = gson.fromJson(jsonAssetMap, HashMap.class);
        } catch (IOException e) {
            Logger.error("Could not find Asset Map at location: " + path);
            e.printStackTrace();
        }
    } 

    public void SerializeAssetMap(String path) {
        Gson gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create();

        String AssetMapJson = gson.toJson(this.assetMap);

        try {
            Files.writeString(Path.of(path), AssetMapJson);
        } catch (IOException e) {
            Logger.error("Failed To Serialize Asset map");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Asset> T GetAsset(int id) {
        if(loadedAssets.containsKey(id)) {
            return (T)loadedAssets.get(id);
        }

        IntermidiateAssetData assetMetaData = this.assetMap.get(id);
        if(assetMetaData == null) {
            Logger.error("Asset with id: " + id + " no found in asset bank aka ist einfach nicht importiert");
            return null;
        }

        Asset loadedAsset = this.LoadAsset(assetMetaData);
        if(loadedAsset != null) loadedAsset.SetId(id);

        return (T)loadedAsset;
    }

    @Override
    public void DisposeAsset(int id) {
        this.loadedAssets.remove(id);
    }
    
}
