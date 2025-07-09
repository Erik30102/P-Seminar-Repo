package com.Editor;

import java.nio.file.Path;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.Editor.EditorWindows.AssetPicker;
import com.Editor.EditorWindows.CodeEditor;
import com.Editor.EditorWindows.ContentBrowser;
import com.Editor.EditorWindows.IEditorImGuiWindow;
import com.Editor.EditorWindows.Inspector;
import com.Editor.EditorWindows.SceneHiarchy;
import com.Editor.EditorWindows.SpriteCreator;
import com.Editor.EditorWindows.TileMapEditor;
import com.Pseminar.Application;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.ScriptingEngine;
import com.Pseminar.Assets.Editor.EditorAssetManager;
import com.Pseminar.Assets.Runtime.AssetPack;
import com.Pseminar.ECS.Component;
import com.Pseminar.ECS.Component.ComponentType;
import com.Pseminar.ECS.Entity;
import com.Pseminar.ECS.IEntityListener;
import com.Pseminar.ECS.Scene;
import com.Pseminar.ECS.Transform;
import com.Pseminar.ECS.BuiltIn.AnimationSpriteComponent;
import com.Pseminar.ECS.BuiltIn.BaseComponent;
import com.Pseminar.ECS.BuiltIn.RidgedBodyComponent;
import com.Pseminar.ECS.BuiltIn.SpriteComponent;
import com.Pseminar.Graphics.RenderApi;
import com.Pseminar.Graphics.RenderBatch;
import com.Pseminar.Graphics.Buffers.FrameBuffer;
import com.Pseminar.Physics.Physics2D;
import com.Pseminar.Physics.PhysicsBody;
import com.Pseminar.Physics.Collider.BoxCollider;
import com.Pseminar.Physics.PhysicsBody.BodyType;
import com.Pseminar.Window.Events.Event;
import com.Pseminar.Window.Events.Event.EventType;
import com.Pseminar.Window.Events.InputEvents.CharEvent;
import com.Pseminar.Window.Events.InputEvents.MouseClickEvent;
import com.Pseminar.Window.Events.InputEvents.MouseMoveEvent;
import com.Pseminar.Window.Events.InputEvents.ScrollEvent;
import com.Pseminar.renderer.OrthographicCamera;
import com.Pseminar.renderer.Shader;

import imgui.gl3.ImGuiImplGl3;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImVec2;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiKey;
import imgui.flag.ImGuiMouseCursor;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;

public class EditorApplication extends Application {

	private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];
	private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

	private IEditorImGuiWindow[] windows;

    private OrthographicCamera camera;
    private RenderBatch spriteBatch;

    private Scene scene;
    private Physics2D physicEngine;

	private FrameBuffer viewportFbo;
	private int viewportWidth = 200,viewportHeight = 200;

	private enum PlayState {
		PLAYING, STOPPED, DEBUGGING
	}

	private PlayState state = PlayState.STOPPED;

    @Override
    public void OnStart() {
		ImGUIINIT();

		AssetPicker.Init();
		new ProjectInfo(new EditorAssetManager(), System.getProperty("user.dir") + "/../ExampleProject", "..\\ScriptingTest\\build\\libs\\ScriptingTest.jar");
        ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).LoadAssetMap();

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

        ScriptingEngine.InitEditor();

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

		viewportFbo = new FrameBuffer(viewportWidth, viewportHeight);

		Inspector inspector = new Inspector();

		// HIER DIE IMGUI WINDOWS REINSCHREIBEN
		windows = new IEditorImGuiWindow[] { new TileMapEditor(), new ContentBrowser(), new SpriteCreator(), new SceneHiarchy(this, inspector), inspector, new CodeEditor()};
    }

	private int[] TexId = new int[] { 0 };

    @Override
    public void OnUpdate(float dt) {
		if (window.shouldClose()) {
            running = false;
        }

		ImGuiBegin();
		
		ImGui.begin("Start Stop");

		if(ImGui.button(state == PlayState.STOPPED ? "Start" : "Stop")) {
			state = state == PlayState.STOPPED ? PlayState.PLAYING : PlayState.STOPPED;
		}


		ImGui.end();

		// ------------------------------------- Da kann alles rein was man so will nur testing grad noch

        ImGui.begin("Generall Settings");
		
		ImGui.dragInt("texId", this.TexId);
		ImGui.image(TexId[0], new ImVec2(200, 200));


		if(ImGui.button("Build Asset Pack")) {
			AssetPack.BuildFromEditor().SaveToDisk("test.assetPack");
		}

		if(ImGui.button("Save Current Scene")) {
			((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).UpdateAsset(this.scene);
		}

        ImGui.end();
		
		// ------------------------------------- die einzelnen extra dinger die andere klassen sind

		for(IEditorImGuiWindow e : this.windows) {
			e.OnImgui();
		}
		
		// -------------------------------------

		ImGui.begin("viewport");

		ImVec2 windowSize = new ImVec2();
		ImGui.getContentRegionAvail(windowSize);

        if (windowSize.x != viewportWidth || windowSize.y != viewportHeight) {
			viewportWidth = (int) windowSize.x;
			viewportHeight = (int) windowSize.y;

			viewportFbo.Resize(viewportWidth, viewportHeight);

			camera.SetZoom(5);
			camera.Resize(viewportWidth, viewportHeight);
		}

		RenderAndUpdateViewport(dt);

		ImGui.image(viewportFbo.GetTexture().GetTextureId(), windowSize.x, windowSize.y, 0, 1, 1, 0);

		ImGui.end();

		// -------------------------------------

        ImGuiEnd();
    }

	private void RenderAndUpdateViewport(float dt) {

		if(state == PlayState.PLAYING) {
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
		}

		viewportFbo.Bind();
        RenderApi.SetViewPort(viewportWidth, viewportHeight);
        RenderApi.setClearColor(0.1f, 0.1f, 0.1f);
        RenderApi.clear();
	
		this.spriteBatch.Begin();


		if(this.scene.GetComponentsByType(ComponentType.SpriteComponent) != null) {
            for(Component component : this.scene.GetComponentsByType(ComponentType.SpriteComponent)) {
                SpriteComponent spriteComponent = (SpriteComponent) component;
				if(spriteComponent.GetSprite() != null) {
					Transform transform = spriteComponent.GetEntity().transform;

					spriteBatch.AddSprite(spriteComponent.GetSprite(), transform);
				}
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

		this.spriteBatch.UpdateAndRender(camera);
		viewportFbo.Unbind();
	}
	
	public Scene GetCurrentScene() {
		return this.scene;
	}

    @Override
    public void OnDispose() {
		
	}
    

	private void ImGUIINIT() {
		ImGui.createContext();


		final ImGuiIO io = ImGui.getIO();

		io.setIniFilename("imgui.ini");
		io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
		io.setConfigFlags(ImGuiConfigFlags.DockingEnable);
		io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors);
		io.setBackendPlatformName("imgui_java_impl_glfw");

		final int[] keyMap = new int[ImGuiKey.COUNT];
		keyMap[ImGuiKey.Tab] = GLFW.GLFW_KEY_TAB;
		keyMap[ImGuiKey.LeftArrow] = GLFW.GLFW_KEY_LEFT;
		keyMap[ImGuiKey.RightArrow] = GLFW.GLFW_KEY_RIGHT;
		keyMap[ImGuiKey.UpArrow] = GLFW.GLFW_KEY_UP;
		keyMap[ImGuiKey.DownArrow] = GLFW.GLFW_KEY_DOWN;
		keyMap[ImGuiKey.PageUp] = GLFW.GLFW_KEY_PAGE_UP;
		keyMap[ImGuiKey.PageDown] = GLFW.GLFW_KEY_PAGE_DOWN;
		keyMap[ImGuiKey.Home] = GLFW.GLFW_KEY_HOME;
		keyMap[ImGuiKey.End] = GLFW.GLFW_KEY_END;
		keyMap[ImGuiKey.Insert] = GLFW.GLFW_KEY_INSERT;
		keyMap[ImGuiKey.Delete] = GLFW.GLFW_KEY_DELETE;
		keyMap[ImGuiKey.Backspace] = GLFW.GLFW_KEY_BACKSPACE;
		keyMap[ImGuiKey.Space] = GLFW.GLFW_KEY_SPACE;
		keyMap[ImGuiKey.Enter] = GLFW.GLFW_KEY_ENTER;
		keyMap[ImGuiKey.Escape] = GLFW.GLFW_KEY_ESCAPE;
		keyMap[ImGuiKey.KeyPadEnter] = GLFW.GLFW_KEY_KP_ENTER;
		keyMap[ImGuiKey.A] = GLFW.GLFW_KEY_A;
		keyMap[ImGuiKey.C] = GLFW.GLFW_KEY_C;
		keyMap[ImGuiKey.V] = GLFW.GLFW_KEY_V;
		keyMap[ImGuiKey.X] = GLFW.GLFW_KEY_X;
		keyMap[ImGuiKey.Y] = GLFW.GLFW_KEY_Y;
		keyMap[ImGuiKey.Z] = GLFW.GLFW_KEY_Z;
		io.setKeyMap(keyMap);

		mouseCursors[ImGuiMouseCursor.Arrow] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.TextInput] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeAll] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNS] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_VRESIZE_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeEW] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNESW] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNWSE] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.Hand] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
		mouseCursors[ImGuiMouseCursor.NotAllowed] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);

		imGuiGl3.init("#version 330 core");
	}

	// TODO: fix typing crashes idk why

    @Override
    protected void OnEventCallback(Event event) {
		ImGuiIO io = ImGui.getIO();
		
		switch (event.getType()) {
			case EventType.KEY_PRESSED:
				// io.setKeysDown(((KeyEvent) event).getKeyCode(), true);
				break;
			case EventType.KEY_RELEASED:
				// io.setKeysDown(((KeyEvent) event).getKeyCode(), false);
				break;
			case EventType.CHAR_EVENT:
				io.addInputCharacter(((CharEvent) event).GetKeyCode());
				break;
			case EventType.MOUSE_MOVED:
				io.setMousePos((float) ((MouseMoveEvent) event).getX(), (float) ((MouseMoveEvent) event).getY());
				break;
			case EventType.MOUSE_PRESSED:
				final boolean[] mouseDown = new boolean[5];

				int button = ((MouseClickEvent) event).getButton();

				mouseDown[0] = button == GLFW.GLFW_MOUSE_BUTTON_1;
				mouseDown[1] = button == GLFW.GLFW_MOUSE_BUTTON_2;
				mouseDown[2] = button == GLFW.GLFW_MOUSE_BUTTON_3;
				mouseDown[3] = button == GLFW.GLFW_MOUSE_BUTTON_4;
				mouseDown[4] = button == GLFW.GLFW_MOUSE_BUTTON_5;

				io.setMouseDown(mouseDown);

				if (!io.getWantCaptureMouse() && mouseDown[1]) {
					ImGui.setWindowFocus(null);
				}
				break;
			case EventType.MOUSE_RELEASED:
				final boolean[] _mouseDown = new boolean[5];

				_mouseDown[0] = false;
				_mouseDown[1] = false;
				_mouseDown[2] = false;
				_mouseDown[3] = false;
				_mouseDown[4] = false;

				io.setMouseDown(_mouseDown);
				break;
			case EventType.MOUSE_SCROLLED:
				io.setMouseWheel(io.getMouseWheel() + (float) ((ScrollEvent) event).GetXOffset());
				io.setMouseWheelH(io.getMouseWheelH() + (float) ((ScrollEvent) event).GetYOffset());
				break;
			default:
				break;
		}
	}

    /// TODO: MOVE TO IMGUI HELPER CLASS
    private void setupDockspace() {
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;

		ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
		ImGui.setNextWindowSize(Application.GetApplication().GetWindow().GetWidth(),
				Application.GetApplication().GetWindow().GetHeight());
		ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
		ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
		windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
				ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
				ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

		ImGui.begin("Dockspace Demo", new ImBoolean(true), windowFlags);
		ImGui.popStyleVar(2);

		ImGui.dockSpace(ImGui.getID("Dockspace"));
    }    

	private float m_time = 0;

    private void ImGuiBegin() {
        ImGuiIO io = ImGui.getIO();
		Application app = Application.GetApplication();
		io.setDisplaySize(app.GetWindow().GetWidth(), app.GetWindow().GetHeight());

		float time = (float) GLFW.glfwGetTime();

		io.setDeltaTime(time - m_time);
		m_time = time;

        imGuiGl3.newFrame();
		ImGui.newFrame();
		// ImGuizmo.beginFrame();
		setupDockspace();
    }

    private void ImGuiEnd() {
		ImGui.end();
		ImGui.render();

		imGuiGl3.renderDrawData(ImGui.getDrawData());
	}
}
