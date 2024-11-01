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

        ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).LoadAssetMap();        
    }

    @Override
    public void OnUpdate() {
        System.out.println(ProjectInfo.GetProjectInfo().GetAssetManager().GetAsset(1652959484).toString());

        running = false;
    }

    @Override
    public void OnDispose() {
        ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).SerializeAssetMap();
    }
}
