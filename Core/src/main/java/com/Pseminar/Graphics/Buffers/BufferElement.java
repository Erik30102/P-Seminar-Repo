package com.Pseminar.Graphics.Buffers;

import org.lwjgl.opengl.GL46;

public class BufferElement {
	public enum DataType {
		FLOAT, VEC2, VEC3, VEC4, MAT3, MAT4, INT, INT2, INT3, INT4, BOOL
	};

	public DataType DataType;
	private int offset;

	public BufferElement(DataType DataType) {
		this.DataType = DataType;
	}

	/**
	 * @return GL46 type of the given Buffer Data type for use with the gpu
	 */
	public int GetOpenGLBaseType() {
		switch (this.DataType) {
			case FLOAT:
				return GL46.GL_FLOAT;
			case VEC2:
				return GL46.GL_FLOAT;
			case VEC3:
				return GL46.GL_FLOAT;
			case VEC4:
				return GL46.GL_FLOAT;
			case MAT3:
				return GL46.GL_FLOAT;
			case MAT4:
				return GL46.GL_FLOAT;
			case INT:
				return GL46.GL_INT;
			case INT2:
				return GL46.GL_INT;
			case INT3:
				return GL46.GL_INT;
			case INT4:
				return GL46.GL_INT;
			case BOOL:
				return GL46.GL_BOOL;
		}
		System.out.println("Unsupported type: " + this.DataType);
		return 0;
	}

	/**
	 * <b>INTERNAL CODE</b>
	 * Do not use is called from the layout itself
	 * 
	 * @param offset
	 */
	public void SetOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return The count of the elements used by the Buffertype
	 */
	public int GetComponentCount() {
		switch (this.DataType) {
			case FLOAT:
				return 1;
			case VEC2:
				return 2;
			case VEC3:
				return 3;
			case VEC4:
				return 4;
			case MAT3:
				return 9;
			case MAT4:
				return 16;
			case INT:
				return 1;
			case INT2:
				return 2;
			case INT3:
				return 3;
			case INT4:
				return 4;
			case BOOL:
				return 1;
		}
		System.out.println("Unsupported DataType for BufferElement");
		return 0;
	}

	@SuppressWarnings("static-access")
	public int GetSize() {
		return this.DataType != DataType.BOOL ? GetComponentCount() * 4 : 1;
	}

	/**
	 * if you have to use this code your in too deep just build your own game engine
	 */
	public int GetOffset() {
		return this.offset;
	}
}