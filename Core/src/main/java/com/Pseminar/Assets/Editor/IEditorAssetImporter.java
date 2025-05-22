package com.Pseminar.Assets.Editor;

import com.Pseminar.Assets.Asset;

public interface IEditorAssetImporter {
    public Asset LoadAsset(IntermidiateAssetData assetMetaData);
    public void SerializeAsset(String path, Asset asset);
}
