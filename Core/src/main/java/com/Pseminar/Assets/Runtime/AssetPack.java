package com.Pseminar.Assets.Runtime;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.Pseminar.Assets.Asset;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Editor.EditorAssetManager;
import com.Pseminar.Assets.Editor.IntermidiateAssetData;
/**
* Die Klasse AssetPack spiegelt ein Paket von Assets wieder,
* welche zur Laufzeit verwendet werden können.
* es werden Informationen von Assets und einer Startszene gespeichert.
*/
public class AssetPack implements Serializable {
    private Map<Integer, Asset> assetInfoMap = new HashMap<>();
    private int startScene;

    public static AssetPack BuildFromEditor() {
        AssetPack assetPack = new AssetPack();

        Map<Integer, IntermidiateAssetData> assets = ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).GetAllAssets();

        for(Map.Entry<Integer, IntermidiateAssetData> entry : assets.entrySet()) {
            Asset asset = ProjectInfo.GetProjectInfo().GetAssetManager().GetAsset(entry.getKey());
            assetPack.assetInfoMap.put(entry.getKey(), asset);
        }

        return assetPack;
    }

    public Map<Integer,Asset> GetAssetInfoMap() {
        return this.assetInfoMap;
    }

    public void SetAssetInfoMap(Map<Integer,Asset> assetInfoMap) {
        this.assetInfoMap = assetInfoMap;
    }

    public int GetStartScene() {
        return this.startScene;
    }

    public void SetStartScene(int startScene) {
        this.startScene = startScene;
    }

    public void SaveToDisk(String path) {
        try (FileOutputStream assetPackStream = new FileOutputStream(path)) {
			ObjectOutputStream out = new ObjectOutputStream(assetPackStream);
			out.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
