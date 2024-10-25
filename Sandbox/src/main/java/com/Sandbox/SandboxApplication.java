package com.Sandbox;

import com.Pseminar.Application;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Editor.EditorAssetManager;

public class SandboxApplication extends Application {

    public static void main(String[] args) {
        new SandboxApplication().Run();
    }

    @Override
    public void OnStart() {
        new ProjectInfo(new EditorAssetManager(), System.getProperty("user.dir") + "/ExampleProject");

        ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).ImportAsset("/images.png");
    }

    @Override
    public void OnUpdate() {
        running = false;
    }

    @Override
    public void OnDispose() {
        ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).SerializeAssetMap();
    }
}
