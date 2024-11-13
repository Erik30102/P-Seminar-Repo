package com.Pseminar.Window;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class InputHandler {
    private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];

    public static void init(long windowHandle) {
        GLFW.glfwSetKeyCallback(windowHandle, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key >= 0 && key < keys.length) {
                    keys[key] = action != GLFW.GLFW_RELEASE;
                }
            }
        });
    }

    public static boolean isKeyPressed(int keyCode) {
        return keyCode >= 0 && keyCode < keys.length && keys[keyCode];
    }

    public static void cleanup() {
        GLFW.glfwSetKeyCallback(0, null).free();
    }
}
