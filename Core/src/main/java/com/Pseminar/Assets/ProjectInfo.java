package com.Pseminar.Assets;

import java.io.Serializable;

public class ProjectInfo implements Serializable {

    private transient static ProjectInfo INSTANCE;
    private transient AssetManager assetManager;

    private String projectPath;

    private int StartSceneAssetId;

    public ProjectInfo(AssetManager assetManager, String projectPath) {
        this.assetManager = assetManager;
        this.projectPath = projectPath;

        INSTANCE = this;
    }

    public static ProjectInfo GetProjectInfo() {
        return INSTANCE;
    }

    public AssetManager GetAssetManager() {
        return this.assetManager;
    }

    public String GetProjectPath() {
        return this.projectPath;
    }

    public int GetStartSceneId() {
        return this.StartSceneAssetId;
    }
}
