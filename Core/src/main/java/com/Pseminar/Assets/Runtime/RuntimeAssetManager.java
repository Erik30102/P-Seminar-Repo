package com.Pseminar.Assets.Runtime;

import java.util.HashMap;
import java.util.Map;

import com.Pseminar.Assets.Asset;
import com.Pseminar.Assets.AssetManager;

// Null bock des mit streams zu machen wird irgendwann anders gemacht
// Wenn sich wer 
public class RuntimeAssetManager implements AssetManager {

    private Map<Integer, Asset> loadedAssets = new HashMap<>();

    @Override
    public <T extends Asset> T GetAsset(int id) {
        throw new UnsupportedOperationException("Unimplemented method 'GetAsset'");
    }

    @Override
    public void DisposeAsset(int id) {
        throw new UnsupportedOperationException("Unimplemented method 'DisposeAsset'");
    } 
    
}
