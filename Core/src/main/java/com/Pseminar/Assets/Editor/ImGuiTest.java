package com.Pseminar.Assets.Editor;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.type.ImInt;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

public class ImGuiTest {
    public static void main(String[] args) {
        // GLFW initialisieren
        if (!GLFW.glfwInit()) {
            System.err.println("GLFW konnte nicht initialisiert werden");
            System.exit(1);
        }

        // Fenster erstellen
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        long window = GLFW.glfwCreateWindow(800, 600, "ImGui Demo", 0, 0);
        if (window == 0) {
            System.err.println("Fenster konnte nicht erstellt werden");
            GLFW.glfwTerminate();
            System.exit(1);
        }

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();

        // ImGui initialisieren
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);

        // Font-Atlas vorbereiten
        ImInt texWidth = new ImInt();
        ImInt texHeight = new ImInt();
        ImInt texComponents = new ImInt();
        ByteBuffer fontTextureData = io.getFonts().getTexDataAsRGBA32(texWidth, texHeight, texComponents);

        // OpenGL-Textur erstellen
        int fontTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTexture);
        GL11.glTexImage2D(
            GL11.GL_TEXTURE_2D,
            0,
            GL11.GL_RGBA,
            texWidth.get(),
            texHeight.get(),
            0,
            GL11.GL_RGBA,
            GL11.GL_UNSIGNED_BYTE,
            fontTextureData
        );
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        io.getFonts().setTexID(fontTexture);

        GLFW.glfwShowWindow(window);

        // Hauptschleife
        while (!GLFW.glfwWindowShouldClose(window)) {
            GLFW.glfwPollEvents();

            int[] fbWidth = new int[1], fbHeight = new int[1];
            GLFW.glfwGetFramebufferSize(window, fbWidth, fbHeight);
            io.setDisplaySize(fbWidth[0], fbHeight[0]);

            ImGui.newFrame();
            ImGui.begin("ImGui Fenster");
            ImGui.text("Hallo Welt!");
            ImGui.end();

            ImGui.render();
            GL11.glViewport(0, 0, fbWidth[0], fbHeight[0]);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            GLFW.glfwSwapBuffers(window);
        }

        // Aufräumen
        ImGui.destroyContext();
        GLFW.glfwTerminate();
    }
}
