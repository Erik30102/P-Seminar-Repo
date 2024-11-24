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
import com.Pseminar.Window.Window;
import com.Pseminar.renderer.Shader;

public class SandboxApplication extends Application {

    private Window window;
    private Shader shader;
    private VertexArray vao;

    public static void main(String[] args) {
        new SandboxApplication().Run();
    }

    @Override
    public void OnStart() {
        new ProjectInfo(new EditorAssetManager(), System.getProperty("user.dir") + "/ExampleProject");

        ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).LoadAssetMap();
        
        window = new Window(800, 600, "Window", true);
        window.init();

        vao = new VertexArray();
        // jeder vertex hat 4 values die ersten 2 sind hier die position und die anderen 2 die textur coordinaten die hier aber in dem shader nur die farbe ausmachen
        VertexBuffer vbo = new VertexBuffer(new float[] { 
                0.5f, 0.5f, 1, 1,      /* V1 */ 
                0.5f, -0.5f, 1, 0,     /* V2 */ 
               -0.5f, -0.5f, 0, 0,     /* V3 */ 
               -0.5f, 0.5f, 0, 1       /* V4 */});
        // Gibt die reinfolge von den vertecies an weil es immer dreiecke ergeben müssen
        IndexBuffer ibo = new IndexBuffer(new int[] {0, 1, 3, 1, 3, 2});

        vbo.SetLayout(new BufferLayout(new BufferElement[] {
            new BufferElement(DataType.VEC2), // Position
            new BufferElement(DataType.VEC2) // Tex coords
        }));

        vao.AddIndexBuffer(ibo);
        vao.AddVertexBuffer(vbo);

        // TODO: remove exceptions
        try {
            shader = new Shader();
            shader.createVertexShader(Shader.loadResource("vertex_shader.glsl"));
            shader.createFragmentShader(Shader.loadResource("fragment_shader.glsl"));
            shader.link();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnUpdate() {
        if (window.shouldClose()) {
            running = false;
        }

        RenderApi.clear();
        RenderApi.setClearColor(0.1f, 0.1f, 0.1f);


        shader.bind();

        vao.bind();
        RenderApi.DrawIndexed(vao);
        
        shader.unbind();


        window.update();
    }

    @Override
    public void OnDispose() {
        ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).SerializeAssetMap();
       
        window.cleanup();
    }
}
