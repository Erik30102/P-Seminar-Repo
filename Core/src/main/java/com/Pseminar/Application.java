package com.Pseminar;

import org.lwjgl.glfw.GLFW;

import com.Pseminar.Window.Window;

public abstract class Application {

    private static Application INSTANCE;
    protected Window window;

    public Application() {
        INSTANCE = this;
    }

    protected boolean running = true;

    public void Run() {
        window = new Window(800, 600, "Window", true);
        window.init();

        GLFW.glfwSetWindowSizeCallback(window.getHandle(), (_, width, height) -> {
            this.OnResize(width, height);
        });

        OnStart();

        while (running) {
            OnUpdate();

            window.update();
        }

        OnDispose();
        window.cleanup();
    }

    public abstract void OnStart();

    public abstract void OnUpdate();

    public abstract void OnDispose();

    public void OnResize(float width, float height) {

    }

    public static Application GetApplication() {
        return INSTANCE;
    }
}
