package com.Pseminar.Graphics;

import org.lwjgl.opengl.GL46;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.Pseminar.Assets.Asset;
import com.Pseminar.TextureLoader.TextureLoader;

public class Texture extends Asset {
    /**
     * Die Klasse Texture ist ein Asset, was man in einem Grafik-Rendering-Prozess verwendet (also z.B.Texturen werden in Sichtbare Bilder umgewandelt) 
     * enthält die Logik für den Umgang mit Texturdaten
    */

    // Nicht finaler code aber kein bock an der OPengllib weiter zu machen deswegene erstmal so 
    private int textureId;
    private String path;

    private int width;
    private int height;

    public enum TextureWrappingMode {
        REPEAT,
        CLAMP_TO_EDGE,
        CLAMP_TO_BORDER
    };

    public enum TextureFiliteringMode {
        NEARST,
        BILINEAR,
    };

    public Texture(String path, TextureFiliteringMode filiteringMode, TextureWrappingMode wrappingMode) {
        this.path = path;
        this.textureId = TextureLoader.createTexture(TextureLoader.loadTexture(path));
    }    

    @Override
    public AssetType GetAssetType() {
        return AssetType.TEXTURE2D;
    }

    @Override
    public void OnDispose() {
        GL46.glDeleteTextures(this.textureId);
    }

    public void Bind(int index) {
        GL46.glActiveTexture(GL46.GL_TEXTURE0 + index);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.textureId);
    }

    public void Unbind() {
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
    }

    public int GetHeight() {
        return this.height;
    }

    public int GetWidth() {
        return this.width;
    }

    @Override
    public String toString() {
        return new String("Texture with id: " + this.textureId);
    }

    private void ApplyParamters(TextureFiliteringMode filtering, TextureWrappingMode wrapping)
    {
        GL46.glTextureParameteri(this.textureId, GL46.GL_TEXTURE_MIN_FILTER,
				this.InternalFilteringToGLFiltering(filtering));
		GL46.glTextureParameteri(this.textureId, GL46.GL_TEXTURE_MAG_FILTER,
				this.InternalFilteringToGLFiltering(filtering));
    
    	GL46.glTextureParameteri(this.textureId, GL46.GL_TEXTURE_WRAP_S, this.InternalWrapModeToGLWrapMode(wrapping));
		GL46.glTextureParameteri(this.textureId, GL46.GL_TEXTURE_WRAP_T, this.InternalWrapModeToGLWrapMode(wrapping));
    }
    
    private int InternalWrapModeToGLWrapMode(TextureWrappingMode wrapMode) {
		switch (wrapMode) {
			case REPEAT:
				return GL46.GL_REPEAT;
			case CLAMP_TO_EDGE:
				return GL46.GL_CLAMP_TO_EDGE;
			case CLAMP_TO_BORDER:
				return GL46.GL_CLAMP_TO_BORDER;
			default:
				return GL46.GL_REPEAT;
		}
	}

    private int InternalFilteringToGLFiltering(TextureFiliteringMode filtering) {
		switch (filtering) {
			case NEARST:
				return GL46.GL_NEAREST;
			case BILINEAR:
				return GL46.GL_LINEAR;
			default:
				return GL46.GL_NEAREST;
		}
	}

    /**
     * BYTE LAYOUT --- nix anfassen bitti
     * 
     * width int
     * height int
     * wrapmode int ordinal Value of enum
     * filtering int ordinal Value of enum
     * 
     * textureData byte[remaing]
     */
    private void readObject(java.io.ObjectInputStream stream)
        throws IOException, ClassNotFoundException {
        
        int textureWidth = stream.readInt();
        int textureHeight = stream.readInt();

        // schon ziemlich arsch aber wird nur in lade sequenzen gecalled
        TextureWrappingMode wrapMode = TextureWrappingMode.values()[stream.readInt()];
        TextureFiliteringMode filteringMode = TextureFiliteringMode.values()[stream.readInt()];

        byte[] texture = stream.readAllBytes();

        this.textureId = TextureLoader.createTexture(ByteBuffer.wrap(texture));
    }

    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        ByteBuffer textureBuffer = TextureLoader.loadTexture(this.path);

        byte[] arr = new byte[textureBuffer.remaining()];

        textureBuffer.get(arr);
        stream.writeObject(arr);
    }
}
