package com.Pseminar.Graphics.Buffers;

/**
 * Beschreibt, wie die Daten in einem Vertex Buffer angeordnet sind.
 * Also: Welche Attribute kommen in welcher Reihenfolge mit welchem Typ.
 * Wird später vom Shader gelesen.
 */
public class BufferLayout {
	private BufferElement[] elements;
	private int stride;

	/**
	 * Erstellt ein neues BufferLayout mit den gegebenen Elementen.
	 * Berechnet intern automatisch Offsets und Stride.
	 *
	 * @param elements Die Attribute, die im Buffer vorkommen (z. B. Position, Farbe, Normalen …)
	 */
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

	/**
	 * Gibt alle Elemente (Attribute) im Layout zurück.
	 *
	 * @return Liste der Buffer-Elemente
	 */
	public BufferElement[] getElements() {
		return this.elements;
	}

	/**
	 * Gibt die Byte-Größe eines kompletten Vertex-Datensatzes zurück.
	 * Also wie viel Abstand zwischen zwei aufeinanderfolgenden Vertices liegt.
	 *
	 * @return Stride in Bytes
	 */
	public int CalculateStride() {
		return this.stride;
	}
}
