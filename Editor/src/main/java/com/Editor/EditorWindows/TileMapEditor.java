package com.Editor.EditorWindows;

import com.Pseminar.BuiltIn.Tilemap;

import imgui.ImGui;

public class TileMapEditor implements IEditorImGuiWindow {

    private Tilemap tilemap;

    @Override
    public void OnImgui() {
        ImGui.begin("TileMapEditor");

        ImGui.columns(2);
        if(ImGui.button("Create New Tilemap")) {

        }
        ImGui.nextColumn();
        if(ImGui.button("Save")) {

        }

        ImGui.columns(1);
        if(ImGui.button("Open SpriteSheet")) {
            
        }

        ImGui.end();
    }
    
}
