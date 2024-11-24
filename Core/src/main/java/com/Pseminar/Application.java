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

	private double lastFrametime = 1;

    private final void InitializeOpengl() {
        window = new Window(800, 600, "Window", true);
        window.init();
    
        GLFW.glfwSetWindowSizeCallback(window.getHandle(), (_, width, height) -> {
            this.OnResize(width, height);
        });
    }

    public void Run() {
        this.InitializeOpengl();
        OnStart();

        while (running) {
			double dt = GLFW.glfwGetTime() - lastFrametime;
			lastFrametime = GLFW.glfwGetTime();

            this.OnUpdate((float)dt);

            window.update();
        }

        OnDispose();
        window.cleanup();
    }

    public abstract void OnStart();

    public abstract void OnUpdate(float dt);

    public abstract void OnDispose();

    public void OnResize(float width, float height) {

    }

    public static Application GetApplication() {
        return INSTANCE;
    }
}
