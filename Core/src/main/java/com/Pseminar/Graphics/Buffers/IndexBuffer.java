package com.Pseminar.Graphics.Buffers;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;

public class IndexBuffer {
	private int bufferId;
	private int size;

	private static IntBuffer createIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

    /**
     * Creates a IndexBuffer for later attachment to a vertex array
     * 
     * @param indicies the indecies 
     */
	public IndexBuffer(int[] indicies) {
		size = indicies.length;

		bufferId = GL46.glGenBuffers();
		GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, bufferId);
		GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, createIntBuffer(indicies), GL46.GL_STATIC_DRAW);
	}

	/**
	 * internal code for binding the indexbuffer
	 */
	public void bind() {
		GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, bufferId);
	}

	/**
	 * internal code for retriving the indecies count for sending the cout of
	 * vertecies to render
	 */
	public int GetCount() {
		return size;
	}

	/**
	 * internal code for unbinding any index buffer
	 */
	public void unbind() {
		GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	/**
	 * delete all the data from the gpu assosicated with the index buffer
	 */
	public void dispose() {
		GL46.glDeleteBuffers(bufferId);
	}
}