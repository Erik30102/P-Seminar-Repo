package com.Editor.EditorWindows;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Asset.AssetType;
import com.Pseminar.Assets.Editor.EditorAssetManager;
import com.Pseminar.ECS.Transform;
import com.Pseminar.Graphics.RenderApi;
import com.Pseminar.Graphics.RenderBatch;
import com.Pseminar.Graphics.Sprite;
import com.Pseminar.Graphics.SpriteSheet;
import com.Pseminar.Graphics.Texture;
import com.Pseminar.Graphics.Buffers.FrameBuffer;
import com.Pseminar.Window.Input;
import com.Pseminar.renderer.OrthographicCamera;
import com.Pseminar.renderer.Shader;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;

public class SpriteCreator implements IEditorImGuiWindow {

    private FrameBuffer fbo;
    private Texture selectedTexture;

    private OrthographicCamera camera;
    private Vector2f cameraPos = new Vector2f();

    private RenderBatch batch;

    private int fboHeight = 200, fboWidth = 200;

    private List<Sprite> generateSprites = new ArrayList<>();

    public SpriteCreator() {
        camera = new OrthographicCamera();
        fbo = new FrameBuffer(fboWidth, fboHeight);
        try {
            Shader shader = new Shader();
            shader.createVertexShader(Shader.loadResource("basic.vert"));
            shader.createFragmentShader(Shader.loadResource("basic.frag"));
            shader.link();

            this.batch = new RenderBatch(5000, shader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int[] resizingTile = new int[2];
    private ImString name = new ImString("test");

    @Override
    public void OnImgui() {
        ImGui.begin("SpriteCreator");

        if(ImGui.beginPopupModal("##spriecreatorSave", new ImBoolean(true),ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoTitleBar)) {
            if(this.generateSprites.size() == 1) {
                ImGui.inputText("Name", name);

                if(ImGui.button("Save")) {
                    ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).AppendAssetToProject(this.generateSprites.get(0), "/assets/" + name.get() + ".sprite");
                    ImGui.closeCurrentPopup();
                }
            }

            if(ImGui.button("discard")) {
                ImGui.closeCurrentPopup();
            }

			ImGui.endPopup();
        }

        if(ImGui.button("Select Texture")) {
			AssetPicker.Open("TEX", AssetType.TEXTURE2D);
        }
		
		if(AssetPicker.Display("TEX")) {
            this.selectedTexture = AssetPicker.GetSelected(Texture.class);
        }
		
        if(ImGui.button("Turn Whole Tex into Sprite")) {
            this.generateSprites.clear();
            
            this.generateSprites.add( new Sprite(selectedTexture, new float[] {
                0,1,
                1,0,
                0,0,
                1,1,
            }));
        }

        ImGui.columns(2);

        ImGui.dragInt2("Size Of Singel Tile", resizingTile);
        ImGui.nextColumn();
        if(ImGui.button("Generate SpriteSheet")) {
            SpriteSheet s = new SpriteSheet(selectedTexture, resizingTile[0], resizingTile[1]);
            this.generateSprites = s.getSprites();
        }

        ImGui.columns(1);

        if(ImGui.button("Save To Disk")) {
            ImGui.openPopup("##spriecreatorSave");
        }

		ImVec2 windowSize = new ImVec2();
		ImGui.getContentRegionAvail(windowSize);

        if (windowSize.x != fboWidth || windowSize.y != fboHeight) {
			fboWidth = (int) windowSize.x;
			fboHeight = (int) windowSize.y;

			fbo.Resize(fboWidth, fboHeight);

			camera.SetZoom(5);
			camera.Resize(fboWidth, fboHeight);
		}
       
        fbo.Bind();
        RenderApi.SetViewPort(fboWidth, fboHeight);
        RenderApi.setClearColor(0.1f, 0.1f, 0.1f);
        RenderApi.clear();

        if(generateSprites != null) {
            this.batch.Begin();

            int i = 0;

            for(Sprite s : this.generateSprites) {
                this.batch.AddSprite(s, new Transform(new Vector2f((i++)+0.2f, 0), new Vector2f(1f,1f), 0));
            }

            this.batch.UpdateAndRender(camera);
        }

        fbo.Unbind();

		ImGui.image(fbo.GetTexture().GetTextureId(), windowSize.x, windowSize.y, 0, 1, 1, 0);
        
        if (ImGui.isWindowHovered() && Input.IsMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_2)) {
			cameraPos.add((float) Input.GetDeltaMouse().x, 0);
			camera.Move(cameraPos);
		}

        ImGui.end();
    }
    
}
