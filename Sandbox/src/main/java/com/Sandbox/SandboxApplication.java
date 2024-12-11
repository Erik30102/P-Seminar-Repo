package com.Sandbox;

import org.lwjgl.glfw.GLFW;

import com.Pseminar.Application;
import com.Pseminar.Logger;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Editor.EditorAssetManager;
import com.Pseminar.ECS.Transform;
import com.Pseminar.Graphics.RenderApi;
import com.Pseminar.Graphics.RenderBatch;
import com.Pseminar.Graphics.Sprite;
import com.Pseminar.Graphics.Texture;
import com.Pseminar.Graphics.Buffers.VertexArray;
import com.Pseminar.Window.Input;
import com.Pseminar.renderer.OrthographicCamera;
import com.Pseminar.renderer.Shader;

public class SandboxApplication extends Application {

    private Shader shader;

    private OrthographicCamera camera;
    private Transform PlayerTransform;
    private Texture testTexture;
    private Sprite sprite;

    private RenderBatch spriteBatch;

    public static void main(String[] args) {
        new SandboxApplication().Run();
    }

    @Override
    public void OnStart() {
        new ProjectInfo(new EditorAssetManager(), System.getProperty("user.dir") + "/ExampleProject");

        ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).LoadAssetMap();

        // TODO: remove exceptions
        try {
            shader = new Shader();
            shader.createVertexShader(Shader.loadResource("basic.vert"));
            shader.createFragmentShader(Shader.loadResource("basic.frag"));
            shader.link();
        } catch (Exception e) {
            e.printStackTrace();
        }

        spriteBatch = new RenderBatch(5000, shader);

        camera = new OrthographicCamera();
        camera.Resize(800, 600);

        PlayerTransform = new Transform();

        testTexture = ProjectInfo.GetProjectInfo().GetAssetManager().GetAsset(1652959484);
        if(testTexture == null) {
            Logger.error("Texture with id: "+ this.testTexture + " Failed to load");
        }

        sprite = new Sprite(testTexture, new float[] {
            0,1,
            1,0,
            0,0,
            1,1,
        });
    }

    @Override
    public void OnUpdate(float dt) {
        if (window.shouldClose()) {
            running = false;
        }

        // Start of Input Logic

        if(Input.IsKeyPressed(GLFW.GLFW_KEY_D)) {
            this.PlayerTransform.move(10f * dt, 0);
        }
        if(Input.IsKeyPressed(GLFW.GLFW_KEY_W)) {
            this.PlayerTransform.move(0.0f , 10f * dt);
        }
        if(Input.IsKeyPressed(GLFW.GLFW_KEY_S)) {
            this.PlayerTransform.move(0.0f, -10f * dt);
        }
        if(Input.IsKeyPressed(GLFW.GLFW_KEY_A)) {
            this.PlayerTransform.move(-10f * dt, 0);
        }

        // Start of rendering

        RenderApi.clear();
        RenderApi.setClearColor(0.1f, 0.1f, 0.1f);

        spriteBatch.Begin();

        spriteBatch.AddSprite(sprite, PlayerTransform);

        spriteBatch.ReloadData();
        spriteBatch.render(camera);
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
