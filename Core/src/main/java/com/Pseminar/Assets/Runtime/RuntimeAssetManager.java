package com.Pseminar.Assets.Runtime;

import java.util.HashMap;
import java.util.Map;

import com.Pseminar.Assets.Asset;
import com.Pseminar.Assets.AssetManager;

// Null bock des mit streams zu machen wird irgendwann anders gemacht
// Wenn sich wer
@SuppressWarnings("unused")
public class RuntimeAssetManager implements AssetManager {

    private Map<Integer, Asset> loadedAssets = new HashMap<>();

    public RuntimeAssetManager(AssetPack assetPack) {
        loadedAssets = assetPack.GetAssetInfoMap();
    }

    /** 
     * @param id
     * @return T
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Asset> T GetAsset(int id) {
        return (T)loadedAssets.get(id);
    }

    /** 
     * @param id
     */
    @Override
    public void DisposeAsset(int id) {
        
    } 
    
}
