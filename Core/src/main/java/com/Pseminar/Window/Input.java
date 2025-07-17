package com.Pseminar.Window;

import java.nio.DoubleBuffer;

import org.joml.Vector2d;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

public class Input {

	private static Vector2d lastMousePosition = new Vector2d();
	private static Vector2d deltaMouse = new Vector2d();

    /** 
     * @param KeyCode
     * @return boolean
     */
    public static boolean IsKeyPressed(int KeyCode) {
        return GLFW.glfwGetKey(Window.GetWindow().getHandle(), KeyCode) == GLFW.GLFW_PRESS;
    }

	/** 
     * @return Vector2d
     */
    public static Vector2d GetDeltaMouse() {
        return deltaMouse;
    }

	/** 
     * @return Vector2d
     */
    public static Vector2d GetMousePos() {
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
		GLFW.glfwGetCursorPos(Window.GetWindow().getHandle(), posX, posY);

		return new Vector2d(posX.get(0), posY.get(0));
    }
    
    public static void OnUpdate() {
        deltaMouse = lastMousePosition.sub(GetMousePos());

		lastMousePosition = GetMousePos();
    }

    /** 
     * @param KeyCode
     * @return boolean
     */
    public static boolean IsMouseButtonPressed(int KeyCode) {
		int state = GLFW.glfwGetMouseButton(Window.GetWindow().getHandle(), KeyCode);
		return state == GLFW.GLFW_PRESS || state == GLFW.GLFW_REPEAT;
    }
}
