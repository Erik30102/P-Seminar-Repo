package com.Sandbox;

import java.util.concurrent.TransferQueue;

import org.lwjgl.glfw.GLFW;

import com.Pseminar.Application;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Editor.EditorAssetManager;
import com.Pseminar.ECS.Transform;
import com.Pseminar.Graphics.RenderApi;
import com.Pseminar.Graphics.Buffers.BufferElement;
import com.Pseminar.Graphics.Buffers.BufferLayout;
import com.Pseminar.Graphics.Buffers.IndexBuffer;
import com.Pseminar.Graphics.Buffers.VertexArray;
import com.Pseminar.Graphics.Buffers.VertexBuffer;
import com.Pseminar.Graphics.Buffers.BufferElement.DataType;
import com.Pseminar.Window.Input;
import com.Pseminar.Window.Window;
import com.Pseminar.renderer.OrthographicCamera;
import com.Pseminar.renderer.Shader;

public class SandboxApplication extends Application {

    private Shader shader;
    private VertexArray vao;

    private OrthographicCamera camera;

    private Transform PlayerTransform;

    public static void main(String[] args) {
        new SandboxApplication().Run();
    }

    @Override
    public void OnStart() {
        new ProjectInfo(new EditorAssetManager(), System.getProperty("user.dir") + "/ExampleProject");

        ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).LoadAssetMap();

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

        camera = new OrthographicCamera();
        camera.Resize(800, 600);

        PlayerTransform = new Transform();
    }

    @Override
    public void OnUpdate() {
        if (window.shouldClose()) {
            running = false;
        }

        // Start of Input Logic

        if(Input.IsKeyPressed(GLFW.GLFW_KEY_D)) {
            this.PlayerTransform.move(0.1f, 0);
        }
        if(Input.IsKeyPressed(GLFW.GLFW_KEY_W)) {
            this.PlayerTransform.move(0.0f, 0.1f);
        }
        if(Input.IsKeyPressed(GLFW.GLFW_KEY_S)) {
            this.PlayerTransform.move(0.0f, -0.1f);
        }
        if(Input.IsKeyPressed(GLFW.GLFW_KEY_A)) {
            this.PlayerTransform.move(-0.1f, 0);
        }

        // Start of rendering

        RenderApi.clear();
        RenderApi.setClearColor(0.1f, 0.1f, 0.1f);

        shader.bind();
        shader.setUniform("projectionMatrix", this.camera.GetProjectionMatrix());
        shader.setUniform("transformMatrix", this.PlayerTransform.GenerateTransformMatrix());

        vao.bind();
        RenderApi.DrawIndexed(vao);
        
        shader.unbind();
    }

    @Override
    public void OnDispose() {
        ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).SerializeAssetMap();  
    }

    @Override
    public void OnResize(float width, float height) {
        RenderApi.SetViewPort(width,height);
        this.camera.Resize(width, height);
    }
}
