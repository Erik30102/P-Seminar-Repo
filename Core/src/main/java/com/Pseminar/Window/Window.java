package com.Pseminar.Window;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private long windowHandle;
    private int width, height;
    private String title;
    private boolean vsync;

    public Window(int width, int height, String title, boolean vsync) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.vsync = vsync;
    }

    public void init() {

		GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("GLFW konnte nicht initialisiert werden");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (NULL == windowHandle) {
            throw new RuntimeException("Konnte GLFW window nicht erstellen");
        }

        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true);
		});

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			glfwGetWindowSize(windowHandle, pWidth, pHeight);

            if (vidMode != null) {
                glfwSetWindowPos(
                    windowHandle,
                    (vidMode.width() - width) / 2,
                    (vidMode.height() - height) / 2
                );
            }
		}

        glfwMakeContextCurrent(windowHandle);

        if (vsync) {
            glfwSwapInterval(1);
        }

        glfwShowWindow(windowHandle);

        GL.createCapabilities();
        setup2DProjection(width, height);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public void update() {}

    public void swapBuffers() {
        glfwSwapBuffers(windowHandle);
    }

    public void cleanup() {
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
    }

    public long getHandle() {
        return windowHandle;
    }

    private void setup2DProjection(int windowWidth, int windowHeight) {
        // Set up an orthographic projection matrix for 2D rendering
        GL11.glMatrixMode(GL11.GL_PROJECTION); // Switch to projection matrix mode
        GL11.glLoadIdentity();                 // Reset the projection matrix
        GL11.glOrtho(0, windowWidth, windowHeight, 0, -1, 1);
        // Sets the 2D projection with (0, 0) as the top-left corner and (windowWidth, windowHeight) as the bottom-right
        GL11.glMatrixMode(GL11.GL_MODELVIEW);  // Switch back to model view matrix mode
        GL11.glLoadIdentity();                 // Reset the model view matrix
    }
}
