package com.Sandbox;

import org.lwjgl.glfw.GLFW;

import com.Pseminar.Application;
import com.Pseminar.Logger;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.ScriptingEngine;
import com.Pseminar.Assets.Editor.EditorAssetManager;
import com.Pseminar.Assets.Editor.Serializer.GsonEditorSceneSerializer;
import com.Pseminar.ECS.Component;
import com.Pseminar.ECS.Entity;
import com.Pseminar.ECS.Scene;
import com.Pseminar.ECS.Transform;
import com.Pseminar.ECS.BuiltIn.BaseComponent;
import com.Pseminar.ECS.BuiltIn.SpriteComponent;
import com.Pseminar.ECS.Component.ComponentType;
import com.Pseminar.Graphics.RenderApi;
import com.Pseminar.Graphics.RenderBatch;
import com.Pseminar.Graphics.Sprite;
import com.Pseminar.Graphics.Texture;
import com.Pseminar.Window.Input;
import com.Pseminar.renderer.OrthographicCamera;
import com.Pseminar.renderer.Shader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SandboxApplication extends Application {

    private OrthographicCamera camera;
    private RenderBatch spriteBatch;

    private ScriptingEngine scriptingEngine;

    private Scene scene;

    public static void main(String[] args) {
        new SandboxApplication().Run();
    }

    @Override
    public void OnStart() {
        new ProjectInfo(new EditorAssetManager(), System.getProperty("user.dir").substring(0, System.getProperty("user.dir").lastIndexOf("\\")) + "/ExampleProject");

        ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).LoadAssetMap();

        // TODO: remove exceptions
        try {
            Shader shader = new Shader();
            shader.createVertexShader(Shader.loadResource("basic.vert"));
            shader.createFragmentShader(Shader.loadResource("basic.frag"));
            shader.link();

            this.spriteBatch = new RenderBatch(5000, shader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.camera = new OrthographicCamera();
        camera.Resize(800, 600);

        Texture testTexture = ProjectInfo.GetProjectInfo().GetAssetManager().GetAsset(1652959484);
        if(testTexture == null) {
            Logger.error("Texture with id: "+ testTexture + " Failed to load");
        }

        Sprite sprite = new Sprite(testTexture, new float[] {
            0,1,
            1,0,
            0,0,
            1,1,
        });

        this.scriptingEngine = new ScriptingEngine("..\\ScriptingTest\\build\\libs\\ScriptingTest.jar");

        this.scene = new Scene();
        Entity exampleEntity = scene.CreateEntity();
        exampleEntity.AddComponent(new SpriteComponent(sprite));
        exampleEntity.AddComponent(this.scriptingEngine.GetNewComponent("com.ScriptingTest.TestComponent"));
    }

    @Override
    public void OnUpdate(float dt) {
        if (window.shouldClose()) {
            running = false;
        }

        if(this.scene.GetComponentsByType(ComponentType.BaseComponent) != null) {
            for(Component component : this.scene.GetComponentsByType(ComponentType.BaseComponent)) {
                BaseComponent spriteComponent = (BaseComponent) component;
                spriteComponent.OnUpdate(dt);
            }
        }

        // Start of rendering

        RenderApi.clear();
        RenderApi.setClearColor(0.1f, 0.1f, 0.1f);

        spriteBatch.Begin();

        if(this.scene.GetComponentsByType(ComponentType.SpriteComponent) != null) {
            for(Component component : this.scene.GetComponentsByType(ComponentType.SpriteComponent)) {
                SpriteComponent spriteComponent = (SpriteComponent) component;
                Transform transform = spriteComponent.GetEntity().transform;

                spriteBatch.AddSprite(spriteComponent.GetSprite(), transform);
            }
        }
        
        spriteBatch.UpdateAndRender(camera);

        if(Input.IsKeyPressed(GLFW.GLFW_KEY_Y)) {
            Gson gson = new GsonBuilder().registerTypeAdapter(Scene.class, new GsonEditorSceneSerializer()).setPrettyPrinting().create();
            Logger.info(gson.toJson(this.scene));
        }
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
