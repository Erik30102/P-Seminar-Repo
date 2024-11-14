package com.Pseminar.Graphics.Buffers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL46;

public class VertexArray {

	private List<VertexBuffer> vertexBuffers = new ArrayList<VertexBuffer>();
	private IndexBuffer indexBuffer;

	private int vertexId;

	public VertexArray() {
		vertexId = GL46.glGenVertexArrays();
	}

	/**
	 * sets the vertex buffer and adds the extra info associated with the layout of
	 * the vertex buffer
	 * 
	 * @param vertexBuffer the vertex buffer to be added
	 * 
	 * @param extraInfo    the extra info associated with the vertex buffer layout
	 */
	public void AddVertexBuffer(VertexBuffer vertexBuffer) {
		this.bind();
		vertexBuffer.bind();

		int index = 0;
		if (vertexBuffer.GetLayout() != null) {
			for (BufferElement element : vertexBuffer.GetLayout().getElements()) {
				GL46.glEnableVertexAttribArray(index);
				GL46.glVertexAttribPointer(index, element.GetComponentCount(), element.GetOpenGLBaseType(), false,
						vertexBuffer.GetLayout().CalculateStride(), element.GetOffset());
				index++;
			}
		}

		vertexBuffers.add(vertexBuffer);
	}

	/**
	 * binds the Vertex buffer for rendering
	 */
	public void bind() {
		GL46.glBindVertexArray(vertexId);
	}

	/**
	 * unbindes the vertex buffer
	 */
	public void unbind() {
		GL46.glBindVertexArray(0);
	}

	/**
	 * Retrieves the list of vertex buffers.
	 *
	 * @return the list of vertex buffers
	 */
	public List<VertexBuffer> getVertexBuffers() {
		return this.vertexBuffers;
	}

	/**
	 * Retrieves the index buffer associated with this object.
	 *
	 * @return the index buffer
	 */
	public IndexBuffer getIndexBuffer() {
		return indexBuffer;
	}

	/**
	 * @param indexBuffer set the index buffer
	 */
	public void AddIndexBuffer(IndexBuffer indexBuffer) {
		this.bind();
		indexBuffer.bind();

		this.indexBuffer = indexBuffer;
	}

	public void Dispose() {
		for (VertexBuffer buffer : vertexBuffers) {
            buffer.dispose();
        }

        if (indexBuffer!= null) {
            indexBuffer.dispose();
        }

        GL46.glDeleteVertexArrays(vertexId);
	}
}