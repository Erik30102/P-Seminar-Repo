package com.Editor.EditorWindows;

import com.Pseminar.Assets.Asset.AssetType;
import com.Pseminar.BuiltIn.Tilemap;
import com.Pseminar.Graphics.SpriteSheet;
import com.Pseminar.Graphics.Buffers.FrameBuffer;

import imgui.ImGui;

public class TileMapEditor implements IEditorImGuiWindow {

    private Tilemap tilemap;
    private SpriteSheet selectedSpriteSheet;

    private int spriteSheetSelecterFBOwidth, spriteSheetSelecterFBOheight;
    private FrameBuffer spriteSheetSelecterFBO;

    private int tileMapEditorFBOwidth, tileMapEditorFBOheight;
    private FrameBuffer tileMapEditorFBO;

    public TileMapEditor() {
        
    }

    private int[] createNewTilemapSize = { 10, 10 };

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

        }

        ImGui.columns(1);
        if(ImGui.button("Open SpriteSheet")) {
            AssetPicker.Open("tilemapSpirtesheet", AssetType.SPRITESHEET);
        }

        if(AssetPicker.Display("tilemapSpirtesheet")) {
            selectedSpriteSheet = AssetPicker.GetSelected(SpriteSheet.class);
        }

        

        ImGui.end();
    }
    
}
