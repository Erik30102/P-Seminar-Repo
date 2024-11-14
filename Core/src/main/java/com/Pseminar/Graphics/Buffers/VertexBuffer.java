package com.Pseminar.Graphics.Buffers;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL46;

public class VertexBuffer {

	private int bufferId;
	private BufferLayout Layout;

    private BUFFER_USAGE bufferUsage;

	public enum BUFFER_USAGE {
		STATIC_DRAW,
		DYNAMIC_DRAW;

		public static int GetUsage(BUFFER_USAGE usage) {
			switch (usage) {
				case STATIC_DRAW:
					return GL46.GL_STATIC_DRAW;
				case DYNAMIC_DRAW:
					return GL46.GL_DYNAMIC_DRAW;
				default:
					return GL46.GL_STATIC_DRAW;
			}
		}
	}

	/**
	 * internal code used for sending the data to the gpu
	 * 
	 * @param data the data to be sent
	 */
	public static FloatBuffer createFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

    /**
     * Create A VertexBuffer
     * 
     * @param vertecies the vertecies of the mesh
     * @param usage Static_DRAW for when the vertecies will not change Dynamic_DRAW when it will change later EG. Sprite Batching
     */
	public VertexBuffer(float[] vertecies, BUFFER_USAGE usage) {
		bufferId = GL46.glGenBuffers();
        this.bufferUsage = usage;

		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, bufferId);
		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, createFloatBuffer(vertecies), BUFFER_USAGE.GetUsage(usage));
	}

	/**
	 * @param vertecies all the vertecies that should be stored on the gpu
	 */
	public VertexBuffer(float[] vertecies) {
		this(vertecies, BUFFER_USAGE.STATIC_DRAW);
	}

    /**
     * Sets the data of the vbo to new vertecies. Only works if buffer Usage is set to Dynamic Draw
     * 
     * @param vertecies
     */
	public void SetData(float[] vertecies) {
        if (this.bufferUsage != BUFFER_USAGE.DYNAMIC_DRAW) {
            System.out.println("Vertex buffer needs to be dynamic to change data after initialization");
            return;
        }
        
		GL46.glBufferSubData(GL46.GL_ARRAY_BUFFER, 0, vertecies);
	}

	/**
	 * set the layout of the vertex array for later use by the shader the data
	 * assosicated with the buffer layout is later specified when adding the
	 * vertexbuffer to the vertex array
	 * 
	 * @param BufferLayout the layout of memory of the vertex buffer
	 */
	public void SetLayout(BufferLayout layout) {
		this.Layout = layout;
	}

	/**
	 * @return layout of the extra info
	 */
	public BufferLayout GetLayout() {
		return this.Layout;
	}

	/**
	 * internal code for binding the vertex buffer
	 */
	public void bind() {
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, bufferId);
	}

	/**
	 * internal code for unbinding any vertex buffer
	 */
	public void unbind() {
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
	}

	/**
	 * to clean up all data form the gpu of the vertex buffer
	 */
	public void dispose() {
		GL46.glDeleteBuffers(bufferId);
	}
}