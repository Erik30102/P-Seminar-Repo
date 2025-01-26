package com.Pseminar.Assets.Editor;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.type.ImString;

public class ImGuiTest extends Application {

    private final ImString textBuffer = new ImString(256); // ImString with a maximum length of 256 characters

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
        ImGui.inputText("Text Field", textBuffer, 0); // Use ImString for mutable text input

        // Save button
        if (ImGui.button("Save")) {
            // Handle save logic (e.g., print the text to the console)
            System.out.println("Saved Text: " + textBuffer.get());
        }

        ImGui.end(); // End the ImGui window
    }

    public static void main(String[] args) {
        launch(new ImGuiTest());
    }
}
