package com.Sandbox;

import org.lwjgl.opengl.GL11;

import com.Pseminar.Application;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Editor.EditorAssetManager;
import com.Pseminar.Window.InputHandler;
import com.Pseminar.Window.Window;
import org.lwjgl.glfw.GLFW;

public class SandboxApplication extends Application {

    Window window;

    public static void main(String[] args) {
        new SandboxApplication().Run();
    }

    @Override
    public void OnStart() {
        new ProjectInfo(new EditorAssetManager(), System.getProperty("user.dir").substring(0, System.getProperty("user.dir").lastIndexOf("\\")) + "/ExampleProject");

        ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).LoadAssetMap();
        
        window = new Window(800, 600, "Window", false);
        window.init();
        InputHandler.init(window.getHandle());
    }

    @Override
    public void OnUpdate() {     

        if(window.shouldClose()){
            running = false;
            GLFW.glfwDestroyWindow(window.getHandle());
            GLFW.glfwTerminate();
        }
        //System.out.println("Path of das scheiß image ist: " + ProjectInfo.GetProjectInfo().GetAssetManager().GetAsset(1652959484).toString());

        
		//GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        //RENDER STUFF

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GLFW.glfwPollEvents();
        window.swapBuffers();
        

    }

    @Override
    public void OnDispose() {
        ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).SerializeAssetMap();
        InputHandler.cleanup();
        window.cleanup();
    }
}
