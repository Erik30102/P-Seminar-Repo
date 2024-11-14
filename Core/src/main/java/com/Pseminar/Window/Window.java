package com.Pseminar.Window;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;


public class Window {
    private long windowHandle;
    private int width, height;
    private String title;
    private boolean vsync;

    private static Window INSTANCE;

    public Window(int width, int height, String title, boolean vsync) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.vsync = vsync;

        INSTANCE = this;
    }

    public void init() {

		GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("GLFW konnte nicht initialisiert werden");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        windowHandle = glfwCreateWindow(width, height, title, 0, 0);
        if (windowHandle == 0) {
            throw new RuntimeException("Konnte GLFW window nicht erstellen");
        }

        //GLFWVidMode vidMode = glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

        GLFW.glfwMakeContextCurrent(windowHandle);

        if (vsync) {
            GLFW.glfwSwapInterval(1);
        }

        GLFW.glfwShowWindow(windowHandle);

        GL.createCapabilities();
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(windowHandle);
    }

    public void update() {
        GLFW.glfwPollEvents();
        GLFW.glfwSwapBuffers(windowHandle);
    }

    public void cleanup() {
        GLFW.glfwDestroyWindow(windowHandle);
        GLFW.glfwTerminate();
    }

    public long getHandle() {
        return windowHandle;
    }

    public static Window GetWindow() {
        return INSTANCE;
    }
}
