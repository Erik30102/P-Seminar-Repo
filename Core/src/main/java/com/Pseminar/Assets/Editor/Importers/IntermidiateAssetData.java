package com.Pseminar.Assets.Editor.Importers;

import com.Pseminar.Assets.Asset.AssetType;

public class IntermidiateAssetData {
    private String path;
    private AssetType type;

    public IntermidiateAssetData(String path, AssetType type) {
        this.path = path;
        this.type = type;
    }

    public String GetPath() { return this.path; }
    public AssetType GetAssetType() { return this.type; }
}
