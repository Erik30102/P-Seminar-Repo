package com.Pseminar.Graphics.Buffers;

/**
 * the layout of the vertex array for later use with shaders
 */
public class BufferLayout {
	private BufferElement[] elements;
	private int stride;

	public BufferLayout(BufferElement[] elements) {
		this.elements = elements;

		int offset = 0;
		stride = 0;
		for (BufferElement element : elements) {
			element.SetOffset(offset);
			stride += element.GetSize();
			offset += element.GetSize();
		}
	}

	public BufferElement[] getElements() {
		return this.elements;
	}

	public int CalculateStride() {
		return this.stride;
	}
}