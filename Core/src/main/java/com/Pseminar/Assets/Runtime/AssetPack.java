package com.Pseminar.Assets.Runtime;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
/**
* Die Klasse AssetPack spiegelt ein Paket von Assets wieder,
* welche zur Laufzeit verwendet werden können.
* es werden Informationen von Assets und einer Startszene gespeichert.
*/
public class AssetPack implements Serializable {
    private Map<Integer, AssetInfo> assetInfoMap;
    private int startScene;

    public Map<Integer,AssetInfo> GetAssetInfoMap() {
        return this.assetInfoMap;
    }

    public void SetAssetInfoMap(Map<Integer,AssetInfo> assetInfoMap) {
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
