package com.Sandbox;

import com.Pseminar.Application;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Editor.EditorAssetManager;
import com.Pseminar.Graphics.RenderApi;
import com.Pseminar.Graphics.Buffers.BufferElement;
import com.Pseminar.Graphics.Buffers.BufferLayout;
import com.Pseminar.Graphics.Buffers.IndexBuffer;
import com.Pseminar.Graphics.Buffers.VertexArray;
import com.Pseminar.Graphics.Buffers.VertexBuffer;
import com.Pseminar.Graphics.Buffers.BufferElement.DataType;
import com.Pseminar.Window.Input;
import com.Pseminar.Window.Window;
import org.lwjgl.glfw.GLFW;

public class SandboxApplication extends Application {

    Window window;
    VertexArray vao;

    public static void main(String[] args) {
        new SandboxApplication().Run();
    }

    @Override
    public void OnStart() {
        new ProjectInfo(new EditorAssetManager(), System.getProperty("user.dir").substring(0, System.getProperty("user.dir").lastIndexOf("\\")) + "/ExampleProject");

        ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).LoadAssetMap();
        
        window = new Window(800, 600, "Window", false);
        window.init();

        vao = new VertexArray();
        VertexBuffer vbo = new VertexBuffer(new float[] { 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f});
        IndexBuffer ibo = new IndexBuffer(new int[] {0, 1, 3, 1, 3, 2});

        vbo.SetLayout(new BufferLayout(new BufferElement[] {
            new BufferElement(DataType.VEC2)
        }));

        vao.AddIndexBuffer(ibo);
        vao.AddVertexBuffer(vbo);
    }

    @Override
    public void OnUpdate() {
        if (Input.IsKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
            running = false;
        }

        RenderApi.clear();
        RenderApi.setClearColor(1, 0, 0);

        vao.bind();
        RenderApi.DrawIndexed(vao);
        
        window.update();

    }

    @Override
    public void OnDispose() {
        ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).SerializeAssetMap();
       
        window.cleanup();
    }
}
