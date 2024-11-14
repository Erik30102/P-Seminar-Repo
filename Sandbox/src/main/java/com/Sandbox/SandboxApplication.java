package com.Sandbox;

import org.lwjgl.opengl.GL46;

import com.Pseminar.Application;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Editor.EditorAssetManager;
import com.Pseminar.Window.Input;
import com.Pseminar.Window.Window;
import org.lwjgl.glfw.GLFW;

public class SandboxApplication extends Application {

    Window window;

    public static void main(String[] args) {
        new SandboxApplication().Run();
    }

    @Override
    public void OnStart() {
        new ProjectInfo(new EditorAssetManager(), System.getProperty("user.dir") + "/ExampleProject");

        ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).LoadAssetMap();
        
        window = new Window(800, 600, "Window", true);
        window.init();
    }

    @Override
    public void OnUpdate() {
        if (window.shouldClose()) {
            running = false;
        }

        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);
		
        GL46.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        window.update();

    }

    @Override
    public void OnDispose() {
        ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).SerializeAssetMap();
       
        window.cleanup();
    }
}
