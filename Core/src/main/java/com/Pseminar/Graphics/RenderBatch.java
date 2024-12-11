package com.Pseminar.Graphics;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;

import com.Pseminar.ECS.Transform;
import com.Pseminar.Graphics.Buffers.BufferElement;
import com.Pseminar.Graphics.Buffers.BufferLayout;
import com.Pseminar.Graphics.Buffers.IndexBuffer;
import com.Pseminar.Graphics.Buffers.VertexArray;
import com.Pseminar.Graphics.Buffers.VertexBuffer;
import com.Pseminar.Graphics.Buffers.BufferElement.DataType;
import com.Pseminar.Graphics.Buffers.VertexBuffer.BUFFER_USAGE;
import com.Pseminar.renderer.OrthographicCamera;
import com.Pseminar.renderer.Shader;

public class RenderBatch {
    private final int batchSize;

    private Shader shader;

    private VertexArray vao;
    private VertexBuffer vbo;

    private boolean hasRoom, hasRoomTextures;

    private float[] vertecies;

    private int[] texSlots;

    private int numOfSprites;
    private int texCount;

    private List<Texture> textures;

    public RenderBatch(int Size, Shader shader) {
        this.shader = shader;
        batchSize = Size;

        hasRoom = true;
        hasRoomTextures = true;

        BufferLayout MemoryLayout = new BufferLayout(new BufferElement[]{
            new BufferElement(DataType.VEC3), // Position länge 3
            new BufferElement(DataType.VEC2), // UVs länge 2
            new BufferElement(DataType.FLOAT), // texId länge 1
        }); // TOTAL 6 Components

        this.vertecies = new float[batchSize * 6 * 4]; // 6000 Sprites jeweils 4 vertecies die 6 componente haben 
    
        this.vao = new VertexArray();
        vao.bind();
        this.vbo = new VertexBuffer(this.vertecies, BUFFER_USAGE.DYNAMIC_DRAW);
        vbo.SetLayout(MemoryLayout);

        IndexBuffer ibo = new IndexBuffer(generateIndecies());

        vao.AddIndexBuffer(ibo);
        vao.AddVertexBuffer(vbo);

        this.texSlots = generateTexSlots(GetMaxBindableTextures());
        this.textures = new ArrayList<>();
    }

    private int[] generateTexSlots(int maxBindableTextures) {
        int[] texSlots = new int[maxBindableTextures];

        for (int i = 0; i < maxBindableTextures; i++) {
			texSlots[i] = i;
		}

        return texSlots;
    }

    public void AddSprite(Sprite sprite, Transform transform) {
        if(!IsTextureAttached(sprite.getTexture())) {
            textures.add(sprite.getTexture());
            texCount++;
            if(texCount >= texSlots.length) {
                hasRoomTextures = false;
            }
        }

        int texIndex = textures.indexOf(sprite.getTexture());

        // Ich mach die transform matrix multiplizerung einfach aufm cpu dann muss man weniger zeug an den gpu senden
        Matrix4f transformMatrix = transform.GenerateTransformMatrix();

        Vector4f[] vec = new Vector4f[] {
            new Vector4f(0.5f, -0.5f, 0f, 1f).mul(transformMatrix),
            new Vector4f(-0.5f, 0.5f, 0f, 1f).mul(transformMatrix),
            new Vector4f(0.5f, 0.5f, 0f, 1f).mul(transformMatrix),
            new Vector4f(-0.5f, -0.5f, 0f, 1f).mul(transformMatrix),
        };

        for(int i = 0; i < 4; i++) {
            int start = numOfSprites * 6 * 4; // weil jeder sprite hat 6 componenten 3 positions componenten 2 uv und 1 textureId component

            vertecies[start + i * 6 + 0] = vec[i].x;
			vertecies[start + i * 6 + 1] = vec[i].y;
			vertecies[start + i * 6 + 2] = vec[i].z;
			vertecies[start + i * 6 + 3] = sprite.getUv()[i*2];
			vertecies[start + i * 6 + 4] = sprite.getUv()[i*2+1];
			vertecies[start + i * 6 + 5] = texIndex;
        }

        this.numOfSprites++;

        if(numOfSprites >= batchSize) {
            hasRoom = false;
        }
    }

    private int[] generateIndecies() {
        int[] indecies = new int[batchSize * 6];

		for (int i = 0; i < batchSize; i++) {
			indecies[i * 6] = i * 4 + 2;
			indecies[i * 6 + 1] = i * 4 + 1;
			indecies[i * 6 + 2] = i * 4 + 0;
			indecies[i * 6 + 3] = i * 4 + 0;
			indecies[i * 6 + 4] = i * 4 + 1;
			indecies[i * 6 + 5] = i * 4 + 3;
		}
		return indecies;    
    }
        
    public void ReloadData() {
        this.vbo.bind();
        this.vbo.SetData(this.vertecies);
        this.vbo.unbind();
    }

    public void render(OrthographicCamera camera) {
        shader.bind();
        shader.setUniform("viewMatrix", camera.GetTransformMatrix());
        shader.setUniform("projectionMatrix", camera.GetProjectionMatrix());

        for(int i = 0; i < texCount; i++) {
            textures.get(i).Bind(i);
        }

        shader.setUniform("textures", texSlots);

        vao.bind();
        RenderApi.DrawIndexed(numOfSprites * 6);
    }

    // Wie viele texturen auf einen shader gebunden werden können werden weil des teilweise von gpu zu gpu unterschiedlich ist
    private int GetMaxBindableTextures() {
        return GL46.glGetInteger(GL46.GL_MAX_TEXTURE_IMAGE_UNITS);
    }

    public void Begin() {
        texCount = 0;
        numOfSprites = 0;
        hasRoom = true;
        hasRoomTextures = true;
    
        textures.clear();
    }

    public boolean HasRoom() {
        return this.hasRoom;
    }

    public boolean IsTextureAttached(Texture tex) {
        return this.textures.contains(tex);
    }

    public boolean HasRoomTextures() {
        return this.hasRoomTextures;
    }
}
