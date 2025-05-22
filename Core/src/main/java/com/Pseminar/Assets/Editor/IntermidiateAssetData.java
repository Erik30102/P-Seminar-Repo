package com.Pseminar.Assets.Editor;

import com.Pseminar.Assets.Asset.AssetType;

public class IntermidiateAssetData {
    private String path;
    private AssetType type;

    /**
     * Damit wird im asset tabel die einzelenne assets beschrieben
     * 
     * @param path der path
     * @param type der type 
     */
    public IntermidiateAssetData(String path, AssetType type) {
        this.path = path;
        this.type = type;
    }

    public String GetPath() { return this.path; }
    public AssetType GetAssetType() { return this.type; }
}
