package com.Pseminar.Graphics;

import org.lwjgl.opengl.GL46;

import com.Pseminar.Graphics.Buffers.BufferElement;
import com.Pseminar.Graphics.Buffers.BufferLayout;
import com.Pseminar.Graphics.Buffers.BufferElement.DataType;
import com.Pseminar.renderer.Shader;

public class RenderBatch {
    private final int MAX_BATCH_SIZE = 6000;

    private Shader shader;

    private boolean hasRoom, hasRoomTextures;

    private float[] vertecies;

    public RenderBatch() {
        hasRoom = true;
        hasRoomTextures = true;

        BufferLayout MemoryLayout = new BufferLayout(new BufferElement[]{
            new BufferElement(DataType.VEC3), // Position
            new BufferElement(DataType.VEC2), // UVs
            new BufferElement(DataType.FLOAT), // texId
        }); // TOTAL 6 Components

        this.vertecies = new float[MAX_BATCH_SIZE * 6 * 4]; // 6000 Sprites jeweils 4 vertecies die 6 componente haben 
    }

    // Wie viele texturen auf einen shader gebunden werden können werden weil des teilweise von gpu zu gpu unterschiedlich ist
    private int GetMaxBindableTextures() {
        return GL46.glGetInteger(GL46.GL_MAX_TEXTURE_IMAGE_UNITS);
    }

    public boolean HasRoom() {
        return this.hasRoom;
    }


    public boolean HasRoomTextures() {
        return this.hasRoomTextures;
    }
}
