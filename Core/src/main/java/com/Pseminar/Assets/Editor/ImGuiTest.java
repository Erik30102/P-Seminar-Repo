package com.Pseminar.Assets.Editor;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.type.ImString;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ImGuiTest extends Application {

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

        // Save button
        if (ImGui.button("Export")) {
            openFileExplorerAndSave(textBuffer.get());
        }
         // Exit-Button
        ImGui.sameLine(); // Optional: Button neben Speichern
        if (ImGui.button("Beenden")) {
            // Saubere Beendigung der Anwendung
            System.exit(0);
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

    private void saveFile() {
        try {
            // Aktuelles Projektverzeichnis
            String projectDir = System.getProperty("user.dir");
            String saveDir = projectDir + "/Speicherdings";
            
            // Verzeichnis erstellen
            Files.createDirectories(Paths.get(saveDir));

            // Eindeutiger Dateiname
            String fileName = "data_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
                ".txt";
            
            // Vollständiger Pfad
            Path fullPath = Paths.get(saveDir, fileName);
            
            // Datei speichern
            Files.writeString(fullPath, textBuffer.get());
            
            System.out.println("Gespeichert: " + fullPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(new ImGuiTest());
    }
}
