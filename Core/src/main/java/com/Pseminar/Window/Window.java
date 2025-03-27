package com.Pseminar.Window;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import com.Pseminar.Window.Events.WindowEvents.WindowCloseEvent;
import com.Pseminar.Window.Events.WindowEvents.WindowResizeEvent;

import static org.lwjgl.glfw.GLFW.*;
import com.Pseminar.Window.Events.InputEvents.KeyEvent;
import com.Pseminar.Window.Events.InputEvents.MouseClickEvent;
import com.Pseminar.Window.Events.InputEvents.MouseEvent;
import com.Pseminar.Window.Events.InputEvents.ScrollEvent;
import com.Pseminar.Window.Events.Event;


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

        GLFW.glfwMakeContextCurrent(windowHandle);

        GLFW.glfwSwapInterval(vsync ? 1 : 0);

        registerCallbacks(windowHandle);

        GLFW.glfwShowWindow(windowHandle);

        GL.createCapabilities();
    }

    public void registerCallbacks(long windowHandle){
        glfwSetFramebufferSizeCallback(windowHandle, (win, width, height) -> {
            WindowResizeEvent event = new WindowResizeEvent(width, height);
            System.out.println(event);
        });

        glfwSetWindowCloseCallback(windowHandle, win -> {
            WindowCloseEvent event = new WindowCloseEvent();
            System.out.println(event);
        });
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
        if (action == GLFW_PRESS) {
            KeyEvent event = new KeyEvent(Event.EventType.KEY_PRESSED, key);
            System.out.println(event);
        } else if (action == GLFW_RELEASE) {
            KeyEvent event = new KeyEvent(Event.EventType.KEY_RELEASED, key);
            System.out.println(event);
        }
    });
	 glfwSetMouseButtonCallback(windowHandle, (window, button, action, mods) -> {
        double[] xpos = new double[1];
        double[] ypos = new double[1];
        glfwGetCursorPos(window, xpos, ypos);

        if (action == GLFW_PRESS) {
            MouseClickEvent event = new MouseClickEvent((int) xpos[0], (int) ypos[0], button);
            System.out.println(event);
        }
    });
	glfwSetCursorPosCallback(windowHandle, (window, xpos, ypos) -> {
        MouseEvent event = new MouseEvent(Event.EventType.MOUSE_MOVED, (int) xpos, (int) ypos);
        System.out.println(event);
    });

    // Scrollen
    glfwSetScrollCallback(windowHandle, (window, xoffset, yoffset) -> {
        ScrollEvent event = new ScrollEvent((float) yoffset);
        System.out.println(event);
    });
	    
    }

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

    public long getHandle() {
        return windowHandle;
    }

    public static Window GetWindow() {
        return INSTANCE;
    }
}
