package com.Pseminar.Assets.Editor;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

import com.Pseminar.Logger;
import com.Pseminar.Assets.Asset;
import com.Pseminar.Assets.AssetManager;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Asset.AssetType;
import com.Pseminar.Assets.Editor.Importers.EditorTexture2dImporter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

// TODO: alles basirend auf ProjectPath machen
public class EditorAssetManager implements AssetManager {

    // bereits geladene Assets
    private transient Map<Integer, Asset> loadedAssets = new HashMap<>();
    private transient Map<AssetType,IEditorAssetImporter> AssetImporters = new HashMap<>(); 
    
    // Ne Map wo drinsteht wie man Asset X importiert
    private Map<Integer, IntermidiateAssetData> assetMap = new HashMap<>();

    public EditorAssetManager() {
        this.SetupAssetImporters();
    }

    /**
     * In Die Methode schreibt mann alle neune importer rein damit die auch als Asset Importer registriert sind
     */
    private void SetupAssetImporters() {
        AssetImporters.put(AssetType.TEXTURE2D, new EditorTexture2dImporter());
    }

    private Asset LoadAsset(IntermidiateAssetData assetMetaData) {
        if(!AssetImporters.containsKey(assetMetaData.GetAssetType())) {
            Logger.error("No importer for type: " + assetMetaData.GetAssetType());
            
            return null;
        }

        Asset loadedAsset = AssetImporters.get(assetMetaData.GetAssetType()).LoadAsset(assetMetaData);
        if(loadedAsset == null){ 
            Logger.error("Can not load Asset at location: " + assetMetaData.GetPath());
        }

        return loadedAsset;
    }  

    /**
     * Importiert ein neues asset basirend auf dem type des files(extension) wenn es dafür ein AssetImporter gibt
     * 
     * @param path Die Path zu dem File
     * @return Null oder das asset falls es importiert werden konnte
     */
    public Asset ImportAsset(String path) {
        AssetType type = Asset.GetAssetTypeFromFilePath(path);
        
        int assetId = new Random().nextInt(Integer.MAX_VALUE);
        while (this.assetMap.keySet().contains(assetId)) {
            assetId = new Random().nextInt(Integer.MAX_VALUE);
        }

        IntermidiateAssetData assetMetaData = new IntermidiateAssetData(path, type);
        Asset asset = LoadAsset(assetMetaData);
        if(asset != null) {
            asset.SetId(assetId);
            loadedAssets.put(assetId, asset);
            assetMap.put(assetId, assetMetaData);
        }

        return asset;
    }

    @SuppressWarnings("unchecked")
    /**
     * lädt die asset map worin die metadata für alle bereits geladenen assets drinstehen
     */
    public void LoadAssetMap() {
        try {
            String jsonAssetMap = Files.readString(Path.of(ProjectInfo.GetProjectInfo().GetProjectPath() + "/AssetMap.amap"));
            Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(this.assetMap.getClass(), new AssetBankSerializer()).create();

            // Glaub des geht garned aber muss ich noch ausprobieren
            this.assetMap = gson.fromJson(jsonAssetMap, this.assetMap.getClass());
        } catch (IOException e) {
            Logger.error("Could not find Asset Map at location: " + ProjectInfo.GetProjectInfo().GetProjectPath() + "/AssetMap.amap");
            e.printStackTrace();
        }
    } 

    /**
     * speichter wieder die asset map damit neu importierte assets auch importiert bleiben
     */
    public void SerializeAssetMap() {
        Gson gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create();

        String AssetMapJson = gson.toJson(this.assetMap);

        try {
            Files.writeString(Path.of(ProjectInfo.GetProjectInfo().GetProjectPath() + "/AssetMap.amap"), AssetMapJson);
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

/**
 * private klasse damit man die asset bank wieder deserializen kann weil gson keinen richtigen map support hat
 */
class AssetBankSerializer implements JsonDeserializer<Map<Integer, IntermidiateAssetData>> {

	@Override
	public Map<Integer, IntermidiateAssetData> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		Map<Integer, IntermidiateAssetData> map = new HashMap<>();

		json.getAsJsonObject().entrySet().forEach(d -> {
			int assetId = Integer.parseInt(d.getKey());
			JsonObject assetData = d.getValue().getAsJsonObject();

			IntermidiateAssetData assetMetaData = context.deserialize(assetData, IntermidiateAssetData.class);

			map.put(assetId, assetMetaData);
		});

		return map;
	}

}