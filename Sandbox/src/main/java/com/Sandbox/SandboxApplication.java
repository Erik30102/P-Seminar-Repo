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
import com.Pseminar.ECS.BuiltIn.AnimationSpriteComponent;
import com.Pseminar.ECS.BuiltIn.BaseComponent;
import com.Pseminar.ECS.BuiltIn.CameraComponent;
import com.Pseminar.ECS.BuiltIn.RidgedBodyComponent;
import com.Pseminar.ECS.BuiltIn.SpriteComponent;
import com.Pseminar.ECS.BuiltIn.TilemapComponent;
import com.Pseminar.ECS.Component.ComponentType;
import com.Pseminar.Graphics.RenderApi;
import com.Pseminar.Graphics.RenderBatch;
import com.Pseminar.Physics.Physics2D;
import com.Pseminar.Physics.PhysicsBody;
import com.Pseminar.Physics.PhysicsBody.BodyType;
import com.Pseminar.Physics.Collider.BoxCollider;
import com.Pseminar.Window.Events.Event;
import com.Pseminar.renderer.OrthographicCamera;
import com.Pseminar.renderer.Shader;

public class SandboxApplication extends Application {

    private RenderBatch spriteBatch;

    private Scene scene;
    private Physics2D physicEngine;

    public static void main(String[] args) {
        new SandboxApplication().Run();
    }

    @Override
    public void OnStart() {
        new ScriptingEngine("..\\ScriptingTest\\build\\libs\\ScriptingTest.jar");
        AssetPack assetPack = AssetPack.AssetPackFromDisk("../Editor/test.assetPack");
        
        new ProjectInfo(new RuntimeAssetManager(assetPack), System.getProperty("user.dir").substring(0, System.getProperty("user.dir").lastIndexOf("\\")) + "/ExampleProject");
        
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

        this.scene = ProjectInfo.GetProjectInfo().GetAssetManager().GetAsset(2012601628);

        this.physicEngine = new Physics2D(new Vector2f(0, 0));

        this.scene.AddListener(ComponentType.RidgedBodyComponent, new IEntityListener<RidgedBodyComponent>() {
            @Override
            public void OnEntityAdded(Entity entity, RidgedBodyComponent component) {
                component.SetBody(new PhysicsBody(BodyType.DYNAMIC));
				component.GetBody().SetPosition(entity.transform.GetPosition());
				component.GetBody().AddCollider(new BoxCollider(1, 1));
            }

            @Override
            public void OnEntityRemoved(Entity entity, RidgedBodyComponent component) {
				component.GetBody().Destroy();
            }
        });

        this.scene.RunAllAddingListeners();

        if(this.scene.GetComponentsByType(ComponentType.TilemapComponent) != null) {
			for(Component component : this.scene.GetComponentsByType(ComponentType.TilemapComponent)) {
				TilemapComponent tilemapComponent = (TilemapComponent) component;

				tilemapComponent.OnStart();
			}
		}

        if(this.scene.GetComponentsByType(ComponentType.BaseComponent) != null) {
            for(Component component : this.scene.GetComponentsByType(ComponentType.BaseComponent)) {
                BaseComponent baseComponent = (BaseComponent) component;
                baseComponent.OnStart();
            }
        }

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

        if(this.scene.GetComponentsByType(ComponentType.RidgedBodyComponent) != null) {
            for(Component component : this.scene.GetComponentsByType(ComponentType.RidgedBodyComponent)) {
                RidgedBodyComponent c = (RidgedBodyComponent)component;

                c.GetEntity().transform.setPosition(c.GetBody().GetPosition());
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
        
        if(this.scene.GetComponentsByType(ComponentType.AnimationComponent) != null) {
            for(Component component : this.scene.GetComponentsByType(ComponentType.AnimationComponent)) {
                AnimationSpriteComponent animationCompoennet = (AnimationSpriteComponent) component;

				animationCompoennet.OnUpdate(dt);

				if(animationCompoennet.GetCurrentSprite() != null) {
					Transform transform = animationCompoennet.GetEntity().transform;

					spriteBatch.AddSprite(animationCompoennet.GetCurrentSprite(), transform);
				}
            }
        }

        if(this.scene.GetComponentsByType(ComponentType.TilemapComponent) != null) {
            for(Component component : this.scene.GetComponentsByType(ComponentType.TilemapComponent)) {
                TilemapComponent tilemapComponent = (TilemapComponent) component;

				Transform transform = tilemapComponent.GetEntity().transform;

				if(tilemapComponent.GetTilemap() == null) break;

				for(int x = 0; x < tilemapComponent.GetTilemap().GetWidth(); x++)  {
					for(int y = 0; y < tilemapComponent.GetTilemap().GetHeight(); y++)  {
						spriteBatch.AddSprite(tilemapComponent.GetTileAt(x, y), transform);

						transform.move(0, 1);
					}
					transform.move(1, -tilemapComponent.GetTilemap().GetHeight());
				}
				transform.move(-tilemapComponent.GetTilemap().GetWidth(), 0);
            }
        }


		if(this.scene.GetComponentsByType(ComponentType.CameraComponent) != null) {
			for(Component component : this.scene.GetComponentsByType(ComponentType.CameraComponent)) {
				CameraComponent cameraComponent = (CameraComponent) component;

				if(cameraComponent.GetActive()) {
					cameraComponent.GetCamera().Move(cameraComponent.GetEntity().transform.GetPosition());

					this.spriteBatch.UpdateAndRender(cameraComponent.GetCamera());
					break;
				}
			}
		}
    }

    @Override
    public void OnDispose() {

    }

    @Override
    public void OnResize(float width, float height) {
        RenderApi.SetViewPort(width,height);
        if(this.scene.GetComponentsByType(ComponentType.CameraComponent) != null) {
            for(Component component : this.scene.GetComponentsByType(ComponentType.CameraComponent)) {
                CameraComponent cameraComponent = (CameraComponent) component;

                cameraComponent.GetCamera().Resize(width, height);
            }
        }
    }

    @Override
    protected void OnEventCallback(Event event) {

    }
}
