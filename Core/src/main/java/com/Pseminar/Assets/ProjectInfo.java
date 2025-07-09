package com.Pseminar.Assets;

import java.io.Serializable;
import java.nio.file.Path;

public class ProjectInfo implements Serializable {

    private transient static ProjectInfo INSTANCE;
    private transient AssetManager assetManager;

    private String projectPath;
    private String compoentJarPath;

    private int StartSceneAssetId;

    // TODO: update javadoc wenn runtime asset manager fertig ist und die gesamte pipline
    /**
     * 
     * 
     * @param assetManager Welchen asset manager es benutzt also 
     * @param projectPath der path von dem project
     */
    public ProjectInfo(AssetManager assetManager, String projectPath) {
        this.assetManager = assetManager;
        this.projectPath = Path.of(projectPath).toString();
        this.compoentJarPath = "INTERNAL";

        INSTANCE = this;
    }

    public ProjectInfo(AssetManager assetManager, String projectPath, String componentJarPath) {
        this(assetManager, projectPath);
        this.compoentJarPath = componentJarPath;
    }

    public String GetComoponentJarPath() {
        return this.compoentJarPath;
    }

    /**
     * @return Die globalen infos zum project/game
     */
    public static ProjectInfo GetProjectInfo() {
        return INSTANCE;
    }

    /**
     * @return den aktellen asset manager weil es davon nur immer einen geben sollte
     */
    public AssetManager GetAssetManager() {
        return this.assetManager;
    }

    public String GetProjectPath() {
        return this.projectPath;
    }

    /**
     * @return welche asset id die start scene hat
     */
    public int GetStartSceneId() {
        return this.StartSceneAssetId;
    }

    public String GetAssetDir() {
        return this.projectPath + "\\assets";
    }
}
