package com.Editor;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.type.ImString;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Editor extends Application {

    private final ImString textBuffer = new ImString(1024); // Text buffer with capacity for multiline text

    @Override
    protected void configure(Configuration config) {
        config.setTitle("GUI Window");
    }

    @Override
    public void process() {
        // Start a new ImGui frame
        ImGui.begin("Text Window"); // Begin a new ImGui window

        ImGui.text("Write your Code down below:");

        // Calculate text field size relative to the window size
        float width = ImGui.getWindowWidth() - 20; // Leave padding
        float height = ImGui.getWindowHeight() * 0.6f; // Use 60% of window height

        // Multiline text field
        ImGui.inputTextMultiline(" EASTER EGG", textBuffer, width, height, 0);

        // Export Button
        if (ImGui.button("Export")) {
            openFileExplorerAndSave(textBuffer.get());
        }
        ImGui.sameLine();

        // Import Button für Textdateien
    if (ImGui.button("Import")) {
        // Datei-Auswahldialog öffnen
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Textdatei importieren");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    
        // Nur Textdateien filtern
        fileChooser.setFileFilter(new FileNameExtensionFilter("Textdateien", "txt"));
    
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Dateiinhalt in Textbuffer laden
                String content = new String(Files.readAllBytes(selectedFile.toPath()));
                textBuffer.set(content);
            } catch (IOException e) {
                System.err.println("Fehler beim Importieren: " + e.getMessage());
            }
        }   
    }

        ImGui.end(); // End the ImGui window
    }

    private void openFileExplorerAndSave(String content) {
        // Use JFileChooser to open a file explorer dialog
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Text File");
        fileChooser.setSelectedFile(new File("output.txt")); // Default file name

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(content);
                System.out.println("File saved: " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error saving file: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new EditorApplication().Run();
        
        //launch(new Editor());
    }
}
