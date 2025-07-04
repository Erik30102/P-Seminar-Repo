package com.Sandbox;

import org.joml.Vector2f;

import com.Pseminar.Application;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.ScriptingEngine;
import com.Pseminar.Assets.Runtime.AssetPack;
import com.Pseminar.Assets.Runtime.RuntimeAssetManager;
import com.Pseminar.ECS.Component;
import com.Pseminar.ECS.Entity;
import com.Pseminar.ECS.IEntityListener;
import com.Pseminar.ECS.Scene;
import com.Pseminar.ECS.Transform;
import com.Pseminar.ECS.BuiltIn.BaseComponent;
import com.Pseminar.ECS.BuiltIn.RidgedBodyComponent;
import com.Pseminar.ECS.BuiltIn.SpriteComponent;
import com.Pseminar.ECS.Component.ComponentType;
import com.Pseminar.Graphics.RenderApi;
import com.Pseminar.Graphics.RenderBatch;
import com.Pseminar.Physics.Physics2D;
import com.Pseminar.Physics.PhysicsBody;
import com.Pseminar.Physics.PhysicsBody.BodyType;
import com.Pseminar.Window.Events.Event;
import com.Pseminar.renderer.OrthographicCamera;
import com.Pseminar.renderer.Shader;

public class SandboxApplication extends Application {

    private OrthographicCamera camera;
    private RenderBatch spriteBatch;

    private Scene scene;
    private Physics2D physicEngine;

    public static void main(String[] args) {
        new SandboxApplication().Run();
    }

    @Override
    public void OnStart() {
        new ScriptingEngine("..\\ScriptingTest\\build\\libs\\ScriptingTest.jar");

        new ProjectInfo(new RuntimeAssetManager(AssetPack.AssetPackFromDisk("../Editor/test.assetPack")), System.getProperty("user.dir").substring(0, System.getProperty("user.dir").lastIndexOf("\\")) + "/ExampleProject");

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

        this.scene = ProjectInfo.GetProjectInfo().GetAssetManager().GetAsset(2012601628);

        this.physicEngine = new Physics2D(new Vector2f(0, 9.81f));

        this.scene.AddListener(ComponentType.RidgedBodyComponent, new IEntityListener<RidgedBodyComponent>() {
            @Override
            public void OnEntityAdded(Entity entity, RidgedBodyComponent component) {
                component.SetBody(new PhysicsBody(BodyType.DYNAMIC));
            }

            @Override
            public void OnEntityRemoved(Entity entity, RidgedBodyComponent component) {

            }
        });

        this.scene.RunAllAddingListeners();
        // AssetPack.BuildFromEditor().SaveToDisk("../test.assetPack");
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

        physicEngine.update(dt);

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
    }

    @Override
    public void OnDispose() {

    }

    @Override
    public void OnResize(float width, float height) {
        RenderApi.SetViewPort(width,height);
        this.camera.Resize(width, height);
    }

    @Override
    protected void OnEventCallback(Event event) {

    }
}
