package com.Pseminar.Window;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import com.Pseminar.Window.Events.WindowEvents.WindowCloseEvent;
import com.Pseminar.Window.Events.WindowEvents.WindowResizeEvent;

import static org.lwjgl.glfw.GLFW.*;

import com.Pseminar.Window.Events.InputEvents.CharEvent;
import com.Pseminar.Window.Events.InputEvents.KeyEvent;
import com.Pseminar.Window.Events.InputEvents.MouseClickEvent;
import com.Pseminar.Window.Events.InputEvents.MouseMoveEvent;
import com.Pseminar.Window.Events.InputEvents.MouseReleaseEvent;
import com.Pseminar.Window.Events.InputEvents.ScrollEvent;
import com.Pseminar.Window.Events.Event;


public class Window {
    private long windowHandle;
    private int width, height;
    private String title;
    private boolean vsync;

    private IEventCallback eventCallback;

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

        GLFW.glfwMakeContextCurrent(windowHandle);

        GLFW.glfwSwapInterval(vsync ? 1 : 0);

        registerCallbacks(windowHandle);

        GLFW.glfwShowWindow(windowHandle);

        GL.createCapabilities();
    }

    /** 
     * @param windowHandle
     */
    public void registerCallbacks(long windowHandle){
        GLFW.glfwSetWindowSizeCallback(windowHandle, (_, width, height) -> {
            this.height = height;
            this.width = width;

            WindowResizeEvent event = new WindowResizeEvent(width, height);
            this.eventCallback.OnEvent(event);
        });

        glfwSetWindowCloseCallback(windowHandle, _ -> {
            WindowCloseEvent event = new WindowCloseEvent();
            this.eventCallback.OnEvent(event);
        });

        glfwSetKeyCallback(windowHandle, (_, key, _, action, _) -> {
            if (action == GLFW_PRESS) {
                KeyEvent event = new KeyEvent(Event.EventType.KEY_PRESSED, key);
                this.eventCallback.OnEvent(event);
            } else if (action == GLFW_RELEASE) {
                KeyEvent event = new KeyEvent(Event.EventType.KEY_RELEASED, key);
                this.eventCallback.OnEvent(event);
            }
        });

        glfwSetCharCallback(windowHandle, (_, codepoint) -> {
			CharEvent event = new CharEvent(codepoint);
            this.eventCallback.OnEvent(event);
        });

        glfwSetMouseButtonCallback(windowHandle, (_, button, action, _) -> {
            if (action == GLFW_PRESS) {
                MouseClickEvent event = new MouseClickEvent(button);
                this.eventCallback.OnEvent(event);
            } else if(action == GLFW_RELEASE) {
                MouseReleaseEvent event = new MouseReleaseEvent(button);
                this.eventCallback.OnEvent(event);
            }
        });
        glfwSetCursorPosCallback(windowHandle, (_, xpos, ypos) -> {
            MouseMoveEvent event = new MouseMoveEvent((int) xpos, (int) ypos);
            this.eventCallback.OnEvent(event);
        });

        // Scrollen
        glfwSetScrollCallback(windowHandle, (_, xoffset, yoffset) -> {
            ScrollEvent event = new ScrollEvent((float) xoffset, (float) yoffset);
            this.eventCallback.OnEvent(event);
        });
    }

    /** 
     * @return boolean
     */
    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(windowHandle);
    }

    public void update() {
        if(Input.IsKeyPressed(GLFW.GLFW_KEY_ESCAPE)){
            glfwSetWindowShouldClose(windowHandle, true);
        }
        GLFW.glfwPollEvents();
        GLFW.glfwSwapBuffers(windowHandle);
    }

    public void cleanup() {
        GLFW.glfwDestroyWindow(windowHandle);
        GLFW.glfwTerminate();
    }

    /** 
     * @return int
     */
    public int GetWidth() {
        return this.width;
    }

    /** 
     * @return int
     */
    public int GetHeight() {
        return this.height;
    }

    /** 
     * @param eventCallback
     */
    public void SetEventCallback(IEventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }

    /** 
     * @return long
     */
    public long getHandle() {
        return windowHandle;
    }

    /** 
     * @return Window
     */
    public static Window GetWindow() {
        return INSTANCE;
    }
}
