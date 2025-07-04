package com.Pseminar;

import org.lwjgl.glfw.GLFW;

import com.Pseminar.Window.IEventCallback;
import com.Pseminar.Window.Window;
import com.Pseminar.Window.Events.Event;
import com.Pseminar.Window.Events.Event.EventType;
import com.Pseminar.Window.Events.WindowEvents.WindowResizeEvent;

public abstract class Application implements IEventCallback {

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
    
        // TODO: move to window

        window.SetEventCallback(this);
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

    public final void OnEvent(Event event) {
        if(event.getType() == EventType.WINDOW_RESIZE) {
            this.OnResize(((WindowResizeEvent)event).getWidth(), ((WindowResizeEvent)event).getHeight());
        }
        if(event.getType() == EventType.WINDOW_CLOSE) {
            this.running = false;
        }

        OnEventCallback(event);
    }

    protected abstract void OnEventCallback(Event event);

    public void OnResize(float width, float height) {

    }

    public static Application GetApplication() {
        return INSTANCE;
    }

    public Window GetWindow() {
        return this.window;
    }
}
