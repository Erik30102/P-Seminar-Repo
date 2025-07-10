package com.Editor.EditorWindows;

import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import com.Pseminar.Logger;
import com.Pseminar.Assets.Asset.AssetType;
import com.Pseminar.Assets.ProjectInfo;
import com.Pseminar.Assets.Editor.EditorAssetManager;
import com.Pseminar.BuiltIn.Tilemap;
import com.Pseminar.ECS.Transform;
import com.Pseminar.Graphics.RenderApi;
import com.Pseminar.Graphics.RenderBatch;
import com.Pseminar.Graphics.Sprite;
import com.Pseminar.Graphics.SpriteSheet;
import com.Pseminar.Graphics.Buffers.FrameBuffer;
import com.Pseminar.Window.Input;
import com.Pseminar.renderer.OrthographicCamera;
import com.Pseminar.renderer.Shader;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;

public class TileMapEditor implements IEditorImGuiWindow {

    private Tilemap tilemap;
    private SpriteSheet selectedSpriteSheet;

    private int spriteSheetSelecterFBOwidth = 1, spriteSheetSelecterFBOheight = 1;
    private FrameBuffer spriteSheetSelecterFBO;
    private OrthographicCamera spriteSheetCamera = new OrthographicCamera();

    private int tileMapEditorFBOwidth = 1, tileMapEditorFBOheight = 1;
    private FrameBuffer tileMapEditorFBO;
    private OrthographicCamera tilemapCamera = new OrthographicCamera();

    private RenderBatch renderBatch;

    public TileMapEditor() {
        spriteSheetSelecterFBO = new FrameBuffer(2, 2);
        tileMapEditorFBO = new FrameBuffer(2, 2);

        try {
            Shader shader = new Shader();
            shader.createVertexShader(Shader.loadResource("basic.vert"));
            shader.createFragmentShader(Shader.loadResource("basic.frag"));
            shader.link();

            this.renderBatch = new RenderBatch(5000, shader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int[] createNewTilemapSize = { 10, 10 };
    private ImString name = new ImString(20);


    @Override
    public void OnImgui() {
        ImGui.begin("TileMapEditor");

        ImGui.columns(2);
        if(ImGui.button("Create New Tilemap")) {
            if(this.selectedSpriteSheet != null) {
                ImGui.openPopup("tilemapCreateNew");
            }
        }

        if(ImGui.beginPopup("tilemapCreateNew")) {
            ImGui.dragInt2("Size", createNewTilemapSize);

            if(ImGui.button("Create")) {
                this.tilemap = new Tilemap(selectedSpriteSheet, createNewTilemapSize[0], createNewTilemapSize[1]);
            }

            ImGui.endPopup();
        }

        ImGui.nextColumn();
        if(ImGui.button("Save")) {
            ImGui.openPopup("##tilemapsaveodisk");
        }

        if(ImGui.beginPopupModal("##tilemapsaveodisk", new ImBoolean(true),ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoTitleBar)) {
            if(tilemap != null) {
                ImGui.inputText("Name", name);

                if(ImGui.button("Save")) {
                    ((EditorAssetManager)ProjectInfo.GetProjectInfo().GetAssetManager()).AppendAssetToProject(this.tilemap, "/assets/" + name.get() + ".tilemap");
                    ImGui.closeCurrentPopup();
                }
            }

            if(ImGui.button("discard")) {
                ImGui.closeCurrentPopup();
            }

			ImGui.endPopup();
        }


        ImGui.columns(1);
        if(ImGui.button("Open SpriteSheet")) {
            AssetPicker.Open("tilemapSpirtesheet", AssetType.SPRITESHEET);
        }

        if(AssetPicker.Display("tilemapSpirtesheet")) {
            selectedSpriteSheet = AssetPicker.GetSelected(SpriteSheet.class);
        }

        ImGui.columns(2);

        ImVec2 spriteSheetwindowSize = new ImVec2();
		ImGui.getContentRegionAvail(spriteSheetwindowSize);

        if (spriteSheetSelecterFBOwidth != (int)((float)spriteSheetwindowSize.x) || (int)((float)spriteSheetwindowSize.y / 5) != spriteSheetSelecterFBOheight) {
			spriteSheetSelecterFBOwidth = (int) ((float)spriteSheetwindowSize.x);
			spriteSheetSelecterFBOheight = (int)  ((float)spriteSheetwindowSize.y / 5);

			spriteSheetSelecterFBO.Resize(spriteSheetSelecterFBOwidth, spriteSheetSelecterFBOheight);

			spriteSheetCamera.SetZoom(2);
			spriteSheetCamera.Resize(spriteSheetSelecterFBOwidth, spriteSheetSelecterFBOheight);
		}

        Vector2d mousePos = Input.GetMousePos();
        // mousePos.sub(ImGui.getWindowPosX(), ImGui.getWindowPosY());
        mousePos.sub(ImGui.getCursorScreenPosX(), ImGui.getCursorScreenPosY());
        mousePos.div(ImGui.getContentRegionAvailX(), ImGui.getContentRegionAvailY() / 5);
        mousePos.mul(2);
        mousePos.sub(1,1);

        RenderSpriteSelector(mousePos);

		ImGui.image(spriteSheetSelecterFBO.GetTexture().GetTextureId(), spriteSheetwindowSize.x, (int)((float)spriteSheetwindowSize.y / 5), 0, 1, 1, 0);

        ImGui.nextColumn();

        if(selectedSpriteSheet != null)
            ImGui.image(selectedSpriteSheet.getTexture().GetTextureId(), ImGui.getContentRegionAvailX(), (int)((float)ImGui.getContentRegionAvailY() / 5), selectedSpriteSheet.getSprite(currentSelectedSpriteIndex).getUv()[2],selectedSpriteSheet.getSprite(currentSelectedSpriteIndex).getUv()[3],selectedSpriteSheet.getSprite(currentSelectedSpriteIndex).getUv()[0],selectedSpriteSheet.getSprite(currentSelectedSpriteIndex).getUv()[1]);

        ImGui.columns(1);

        ImVec2 windowSize = new ImVec2();
		ImGui.getContentRegionAvail(windowSize);

        if (windowSize.x != tileMapEditorFBOwidth  || windowSize.y != tileMapEditorFBOheight ) {
			tileMapEditorFBOwidth = (int) windowSize.x;
			tileMapEditorFBOheight = (int)  windowSize.y;

			tileMapEditorFBO.Resize(tileMapEditorFBOwidth, tileMapEditorFBOheight);

			tilemapCamera.SetZoom(5);
			tilemapCamera.Resize(tileMapEditorFBOwidth, tileMapEditorFBOheight);
		}


        Vector2d mousePos2 = Input.GetMousePos();
        // mousePos.sub(ImGui.getWindowPosX(), ImGui.getWindowPosY());
        mousePos2.sub(ImGui.getCursorScreenPosX(), ImGui.getCursorScreenPosY());
        mousePos2.div(ImGui.getContentRegionAvailX(), ImGui.getContentRegionAvailY() );
        mousePos2.mul(2);
        mousePos2.sub(1,1);

        RenderTileMapEditor(mousePos2);

		ImGui.image(tileMapEditorFBO.GetTexture().GetTextureId(), windowSize.x, windowSize.y, 0, 1, 1, 0);
    
        ImGui.end();

    }
    
    private int currentSelectedSpriteIndex = 0;

    public void RenderSpriteSelector(Vector2d mousePos) {
        if(mousePos.x >= -1 && mousePos.x <= 1 && mousePos.y >= -1 && mousePos.y <= 1) {
            if(Input.IsMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
                Vector4f newPos = new Vector4f((float)mousePos.x, (float)mousePos.y, 0, 1);
                newPos.mul(spriteSheetCamera.GetInversTransformationMatrix().mul(spriteSheetCamera.GetInversProjectionMatrix(), new Matrix4f()));

                int index = (int) Math.floor(newPos.x + 0.5f);
                if(index >= 0 && index < this.selectedSpriteSheet.getSprites().size()) {
                    currentSelectedSpriteIndex = index;
                }
            }
            if(Input.IsMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_3)) {
                spriteSheetCamera.MoveBy(new Vector2d(Input.GetDeltaMouse().x * 0.7, 0));
            }
        }

        spriteSheetSelecterFBO.Bind();
        RenderApi.SetViewPort(spriteSheetSelecterFBOwidth, spriteSheetSelecterFBOheight);
        RenderApi.setClearColor(0.1f, 0.1f, 0.1f);
        RenderApi.clear();

        renderBatch.Begin();

        if(selectedSpriteSheet != null) {
            int i = 0;

            for(Sprite s : this.selectedSpriteSheet.getSprites()) {
                this.renderBatch.AddSprite(s, new Transform(new Vector2f((i++)+0.2f, 0), new Vector2f(1f,1f), 0,2));
            }
        }

        renderBatch.UpdateAndRender(spriteSheetCamera);

        spriteSheetSelecterFBO.Unbind();
    }

    public void RenderTileMapEditor(Vector2d mousePos) {
        Vector4f positionOfMouseCursor = new Vector4f(20000, 200000,0,0);

        if(mousePos.x >= -1 && mousePos.x <= 1 && mousePos.y >= -1 && mousePos.y <= 1) {
            if(Input.IsMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_3)) {
                tilemapCamera.MoveBy(new Vector2d(Input.GetDeltaMouse().x * 0.7, Input.GetDeltaMouse().y * 0.7));
            }

            Vector4f newPos = new Vector4f((float)mousePos.x, (float)-mousePos.y, 0, 1);
            newPos.mul(tilemapCamera.GetInversTransformationMatrix().mul(tilemapCamera.GetInversProjectionMatrix(), new Matrix4f()));
        
            positionOfMouseCursor = newPos;
            if(Input.IsMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1)) {
                int x = Math.round(positionOfMouseCursor.x);
                int y = Math.round(positionOfMouseCursor.y);

                if(x >= 0 && x < tilemap.GetWidth() && y >= 0 && y < tilemap.GetHeight()) {
                    tilemap.SetTile(x,y, currentSelectedSpriteIndex);
                }
            }
        }

        tileMapEditorFBO.Bind();
        RenderApi.SetViewPort(tileMapEditorFBOwidth, tileMapEditorFBOheight);
        RenderApi.setClearColor(0.1f, 0.1f, 0.1f);
        RenderApi.clear();

        renderBatch.Begin();

        if(tilemap != null) {
            for(int x = 0; x < tilemap.GetWidth(); x++)  {
                for(int y = 0; y < tilemap.GetHeight(); y++)  {
                    renderBatch.AddSprite(tilemap.GetSpritesheet().getSprite(tilemap.GetTile(x, y)), new Transform(new Vector2f(x,y), new Vector2f(1), 0));
                }
            }

            renderBatch.AddSprite(tilemap.GetSpritesheet().getSprite(currentSelectedSpriteIndex), new Transform(new Vector2f(Math.round(positionOfMouseCursor.x),Math.round(positionOfMouseCursor.y)), new Vector2f(1), 0));
        }

        renderBatch.UpdateAndRender(tilemapCamera);

        tileMapEditorFBO.Unbind();
    }
}
