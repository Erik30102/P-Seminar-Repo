package com.Pseminar.Graphics;

import org.lwjgl.opengl.GL46;

import com.Pseminar.Graphics.Buffers.VertexArray;

public class RenderApi {
    public static void setClearColor(float r, float g, float b) {
		GL46.glClearColor(r, g, b, 1);
	}

	public static void clear() {
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
	}

	public static void DrawIndexed(VertexArray vertexArray) {
		DrawIndexed(vertexArray.getIndexBuffer().GetCount());
	}

	public static void DrawIndexed(int count) {
		GL46.glDrawElements(GL46.GL_TRIANGLES, count, GL46.GL_UNSIGNED_INT, 0);
	}
}
