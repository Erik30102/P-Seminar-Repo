package com.Pseminar.Graphics;

import org.lwjgl.opengl.GL46;

import com.Pseminar.Graphics.Buffers.VertexArray;

public class RenderApi {
    /** 
	 * @param r
	 * @param g
	 * @param b
	 */
	public static void setClearColor(float r, float g, float b) {
		GL46.glClearColor(r, g, b, 1);
	}

	public static void clear() {
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
	}

	/** 
	 * @param vertexArray
	 */
	public static void DrawIndexed(VertexArray vertexArray) {
		DrawIndexed(vertexArray.getIndexBuffer().GetCount());
	}

	/** 
	 * @param count
	 */
	public static void DrawIndexed(int count) {
		GL46.glDrawElements(GL46.GL_TRIANGLES, count, GL46.GL_UNSIGNED_INT, 0);
	}

    /** 
	 * @param width
	 * @param height
	 */
	public static void SetViewPort(float width, float height) {
		GL46.glViewport(0, 0, (int)width, (int)height);
	}
}
