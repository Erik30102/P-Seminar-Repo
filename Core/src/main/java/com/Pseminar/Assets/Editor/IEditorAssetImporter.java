package com.Pseminar.Assets.Editor;

import com.Pseminar.Assets.Asset;

public interface IEditorAssetImporter {
    /**
     * Läd ein asset basirend auf den existirenden meta dataen
     * 
     * @param assetMetaData die meta datem
     * @return das asset oder null wenn nix klappt
     */
    public Asset LoadAsset(IntermidiateAssetData assetMetaData);

    /**
     * Speichert ein asset am path WICHTIG: hier absuluter path also den gesamten path angeben
     * 
     * @param path der path
     * @param asset das asset was serialized werden muss
     */
    public void SerializeAsset(String path, Asset asset);
}
