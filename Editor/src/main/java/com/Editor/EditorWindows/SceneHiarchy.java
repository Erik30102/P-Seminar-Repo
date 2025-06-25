package com.Editor.EditorWindows;

import com.Editor.EditorApplication;
import com.Pseminar.ECS.Entity;

import imgui.ImGui;

public class SceneHiarchy implements IEditorImGuiWindow {

    private transient EditorApplication editor;

    public SceneHiarchy(EditorApplication editor) {
        this.editor = editor;
    }

    @Override
    public void OnImgui() {
        ImGui.begin("Scene Hiarchy");

        if(ImGui.button("Create New Entity")) {
            this.editor.GetCurrentScene().CreateEntity();
        }

        ImGui.separator();

        for (Entity entity : editor.GetCurrentScene().GetEntites()) {
            if (ImGui.button(entity.GetName())) {
                // TODO: mit inspector dann verlinken
            }
        }

        ImGui.end();
    }
    
}
