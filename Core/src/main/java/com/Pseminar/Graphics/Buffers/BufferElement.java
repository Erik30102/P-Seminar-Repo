package com.Pseminar.Graphics.Buffers;

import org.lwjgl.opengl.GL46;

/**
 * Repräsentiert ein Element in einem Vertex/Index-Buffer – also z. B. ein float, vec3, mat4, etc.
 */
public class BufferElement {

    /**
     * Einfach alle Typen, die ein Attribut im Buffer haben kann.
     */
    public enum DataType {
        FLOAT, VEC2, VEC3, VEC4, MAT3, MAT4, INT, INT2, INT3, INT4, BOOL
    }

    public DataType DataType;
    private int offset;

    /**
     * Erstellt ein BufferElement mit gegebenem Typ.
     *
     * @param DataType Datentyp (z. B. VEC3, INT, MAT4 …)
     */
    public BufferElement(DataType DataType) {
        this.DataType = DataType;
    }

    /**
     * Gibt den OpenGL-Typ zurück, den man für diesen Datentyp benutzen muss.
     * Braucht man z. B. bei glVertexAttribPointer.
     *
     * @return GL46-konformer Typ (GL_FLOAT, GL_INT, …)
     */
    public int GetOpenGLBaseType() {
        switch (this.DataType) {
            case FLOAT:
            case VEC2:
            case VEC3:
            case VEC4:
            case MAT3:
            case MAT4:
                return GL46.GL_FLOAT;
            case INT:
            case INT2:
            case INT3:
            case INT4:
                return GL46.GL_INT;
            case BOOL:
                return GL46.GL_BOOL;
        }
        System.out.println("Unsupported type: " + this.DataType);
        return 0;
    }

    /**
     * Intern genutzt – setzt den Offset im Buffer.
     * Wird von BufferLayout automatisch gesetzt.
     *
     * @param offset Offset in Bytes
     */
    public void SetOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Gibt zurück, wie viele einzelne Komponenten der Typ hat.
     * Z. B. VEC3 → 3, MAT4 → 16, BOOL → 1
     *
     * @return Anzahl der Komponenten
     */
    public int GetComponentCount() {
        switch (this.DataType) {
            case FLOAT:
            case INT:
            case BOOL:
                return 1;
            case VEC2:
            case INT2:
                return 2;
            case VEC3:
            case INT3:
                return 3;
            case VEC4:
            case INT4:
                return 4;
            case MAT3:
                return 9;
            case MAT4:
                return 16;
        }
        System.out.println("Unsupported DataType for BufferElement");
        return 0;
    }

    /**
     * Rechnet aus, wie viele Bytes der Typ belegt.
     * Alles ist 4 Byte pro Komponente – außer BOOL (der ist nur 1 Byte).
     *
     * @return Größe in Bytes
     */
    @SuppressWarnings("static-access")
    public int GetSize() {
        return this.DataType != DataType.BOOL ? GetComponentCount() * 4 : 1;
    }

    /**
     * Gibt den Offset zurück.
	 * Wird nimand jemals brauch ist engine rendering code
     *
     * @return Offset in Bytes
     */
    public int GetOffset() {
        return this.offset;
    }
}
