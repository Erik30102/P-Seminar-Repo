package com.Pseminar.Assets;

import java.io.Serializable;

public class ProjectInfo implements Serializable {

    private transient static ProjectInfo INSTANCE;
    private transient AssetManager assetManager;

    private int StartSceneAssetId;

    public static ProjectInfo GetProjectInfo() {
        return INSTANCE;
    }

    public AssetManager GetAssetManager() {
        return this.assetManager;
    }

    public int GetStartSceneId() {
        return this.StartSceneAssetId;
    }
}
