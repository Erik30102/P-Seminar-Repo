package com.Editor.EditorWindows;

import com.Editor.EditorApplication;
import com.Pseminar.ECS.Entity;

import imgui.ImGui;

public class SceneHiarchy implements IEditorImGuiWindow {

    private transient EditorApplication editor;
    private transient Inspector inspector;

    public SceneHiarchy(EditorApplication editor, Inspector inspector) {
        this.editor = editor;
        this.inspector = inspector;
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
                inspector.SetEntity(entity);
            }
        }

        ImGui.end();
    }
    
}
