package com.Pseminar.Assets;

public interface AssetManager {
    /**
     * Returned das asset und läds wenns nicht schon importiert ist
     * 
     * @param <T> 
     * @param id die id des assets
     * @return
     */
    public <T extends Asset> T GetAsset(int id);

    /**
     * dispoese asset wenns nichtmehr benötigt wird wird eigentlich automatisch gemacht durchs ref counting system in den assets
     * 
     * @param id
     */
    public void DisposeAsset(int id);
}
