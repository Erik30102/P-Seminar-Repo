package com.Pseminar.Window;

import org.lwjgl.glfw.GLFW;

public class Input {

    public static boolean IsKeyPressed(int KeyCode) {
        return GLFW.glfwGetKey(Window.GetWindow().getHandle(), KeyCode) == GLFW.GLFW_PRESS;
    }
}
