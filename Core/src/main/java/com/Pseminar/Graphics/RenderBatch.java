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
            new BufferElement(DataType.VEC4)
        }); // TOTAL 6 Components

        this.vertecies = new float[batchSize * 10 * 4]; // 6000 Sprites jeweils 4 vertecies die 6 componente haben 
    
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

    /** 
     * @param maxBindableTextures
     * @return int[]
     */
    private int[] generateTexSlots(int maxBindableTextures) {
        int[] texSlots = new int[maxBindableTextures];

        for (int i = 0; i < maxBindableTextures; i++) {
			texSlots[i] = i;
		}

        return texSlots;
    }

    private List<SpriteRenderEntry> renderEntries = new ArrayList<>();

    /** 
     * @param sprite
     * @param transform
     */
    public void AddSprite(Sprite sprite, Transform transform) {
        AddSprite(sprite, transform, new Vector4f(1));
    }

    /** 
     * @param sprite
     * @param transform
     * @param tint
     */
    public void AddSprite(Sprite sprite, Transform transform, Vector4f tint) {
        renderEntries.add(new SpriteRenderEntry(sprite, transform.GenerateTransformMatrix(), transform.GetZIndex(), tint));
    }


    /** 
     * @param sprite
     * @param transformMatrix
     * @param tint
     */
    private void AddSpriteToVertexArray(Sprite sprite, Matrix4f transformMatrix, Vector4f tint) {
        if(!IsTextureAttached(sprite.getTexture())) {
            textures.add(sprite.getTexture());
            texCount++;
            if(texCount >= texSlots.length) {
                hasRoomTextures = false;
            }
        }

        int texIndex = textures.indexOf(sprite.getTexture());

        Vector4f[] vec = new Vector4f[] {
            new Vector4f(0.5f, -0.5f, 0f, 1f).mul(transformMatrix),
            new Vector4f(-0.5f, 0.5f, 0f, 1f).mul(transformMatrix),
            new Vector4f(0.5f, 0.5f, 0f, 1f).mul(transformMatrix),
            new Vector4f(-0.5f, -0.5f, 0f, 1f).mul(transformMatrix),
        };

        for(int i = 0; i < 4; i++) {
            int start = numOfSprites * 10 * 4; // weil jeder sprite hat 6 componenten 3 positions componenten 2 uv und 1 textureId component

            vertecies[start + i * 10 + 0] = vec[i].x;
			vertecies[start + i * 10 + 1] = vec[i].y;
			vertecies[start + i * 10 + 2] = vec[i].z;
			vertecies[start + i * 10 + 3] = sprite.getUv()[i*2];
			vertecies[start + i * 10 + 4] = sprite.getUv()[i*2+1];
			vertecies[start + i * 10 + 5] = texIndex;
			vertecies[start + i * 10 + 6] = tint.x;            
			vertecies[start + i * 10 + 7] = tint.y;            
			vertecies[start + i * 10 + 8] = tint.z;            
			vertecies[start + i * 10 + 9] = tint.w;            
        }

        this.numOfSprites++;

        if(numOfSprites >= batchSize) {
            hasRoom = false;
        }
    }

    /** 
     * @return int[]
     */
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
        this.renderEntries.sort((a,b) -> Float.compare(a.zIndex, b.zIndex));

        for (SpriteRenderEntry spriteRenderEntry : renderEntries) {
            this.AddSpriteToVertexArray(spriteRenderEntry.sprite, spriteRenderEntry.transform, spriteRenderEntry.tint);
        }

        this.vbo.bind();
        this.vbo.SetData(this.vertecies);
        this.vbo.unbind();
    }

    /** 
     * @param camera
     */
    public void UpdateAndRender(OrthographicCamera camera) {
        this.ReloadData();
        this.render(camera);

        renderEntries.clear();
    }

    /** 
     * @param camera
     */
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

    /** 
     * Wie viele texturen auf einen shader gebunden werden können werden weil des teilweise von gpu zu gpu unterschiedlich ist
     * @return int
     */
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

    /** 
     * @return boolean
     */
    public boolean HasRoom() {
        return this.hasRoom;
    }

    /** 
     * @param tex
     * @return boolean
     */
    public boolean IsTextureAttached(Texture tex) {
        return this.textures.contains(tex);
    }

    /** 
     * @return boolean
     */
    public boolean HasRoomTextures() {
        return this.hasRoomTextures;
    }
}

class SpriteRenderEntry {
    public Sprite sprite;
    public Matrix4f transform;
    public Vector4f tint;

    public float zIndex;

    public SpriteRenderEntry(Sprite sprite, Matrix4f transform,float zIndex, Vector4f tint) {
        this.sprite = sprite;
        this.transform = transform;
        this.zIndex = zIndex;
        this.tint = tint;
    }
}