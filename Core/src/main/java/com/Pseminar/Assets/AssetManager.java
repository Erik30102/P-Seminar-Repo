package com.Pseminar.Assets;

public interface AssetManager {
    /**
     * Gets Asset and loads it if not already loaded
     * 
     * @param <T> this does no typechecking if it cannot cast it will crash
     * @param id
     * @return
     */
    public <T extends Asset> T GetAsset(int id);

    public void DisposeAsset(int id);
}
