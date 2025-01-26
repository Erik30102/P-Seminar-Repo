package com.Pseminar.Assets.Editor;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;

public class ImGuiTest extends Application {

    private final StringBuilder textBuffer = new StringBuilder("Default Text"); // Text field buffer

    @Override
    protected void configure(Configuration config) {
        config.setTitle("Hello ImGui Window");
    }

    @Override
    public void process() {
        // Start a new ImGui frame
        ImGui.begin("Example Window"); // Begin a new ImGui window
        
        ImGui.text("This is a simple ImGui window in Java.");
        ImGui.text("You can add more controls here!");

        // Input text field
        ImGui.inputText("Text Field", textBuffer, 0); // Pass 0 for no flags

        // Save button
        if (ImGui.button("Save")) {
            // Handle save logic (e.g., print the text to the console)
            System.out.println("Saved Text: " + textBuffer.toString());
        }

        ImGui.end(); // End the ImGui window
    }

    public static void main(String[] args) {
        launch(new ImGuiTest());
    }
}
