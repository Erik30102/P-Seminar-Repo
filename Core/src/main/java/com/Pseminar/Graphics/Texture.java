package com.Pseminar.Graphics;

import org.lwjgl.opengl.GL46;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.Pseminar.Logger;
import com.Pseminar.Assets.Asset;

import imgui.assertion.ImAssertCallback;

class ImageData {
    public ByteBuffer buffer;
    public int width;
    public int heigth;

    public String path;
}

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

    private TextureFiliteringMode filiteringMode;
    private TextureWrappingMode wrappingMode;

    public enum TextureWrappingMode {
        REPEAT,
        CLAMP_TO_EDGE,
        CLAMP_TO_BORDER
    };

    public enum TextureFiliteringMode {
        NEARST,
        BILINEAR,
    };

    /**
     * Läd texture von festplatte mit default filtering und wrapping modus
     * 
     * @param path der path eigentlich logisch
     */
    public Texture(String path) {
        this(path, TextureFiliteringMode.NEARST, TextureWrappingMode.CLAMP_TO_BORDER);
    }

    /**
     * Läd texture von disk
     * 
     * @param path der path 
     * @param filiteringMode ob die pixel interpoliert werden sollen also obs pixel oder so ne übergang geben soll. Generell Eigentlich einfach pixel art = NEAREST alles andere BILINEAR
     * @param wrappingMode wie das image sich verhalten soll bei uvs höher als 1 einfach googeln am besten für ne visuelle demonstartaion
     */
    public Texture(String path, TextureFiliteringMode filiteringMode, TextureWrappingMode wrappingMode) {
        this.path = path;

        ImageData imData = GetImageDataFromPath(path);
        if(imData.buffer == null) {
            Logger.error("could not load file on path, " + path);
        }

        CreateImageFromData(imData.buffer, imData.width, imData.heigth, filiteringMode, wrappingMode);
    }    

    private void CreateImageFromData(ByteBuffer textureBuffer, int width, int height, TextureFiliteringMode filiteringMode, TextureWrappingMode frapping) {
        this.height = height;
        this.width = width;

        this.textureId = GL46.glGenTextures();
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.textureId);

        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGBA, width, height, 0,
                          GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, textureBuffer);

        ApplyParamters(filiteringMode, frapping);
    }

    private ImageData GetImageDataFromPath(String path) {
        ImageData imData = new ImageData();
        imData.path = path;

        try {
            BufferedImage image = ImageIO.read(new File(path));
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

            imData.heigth = imageHeight;
            imData.width = imageWidth;

            int[] pixels = new int[imageWidth * imageHeight];
            image.getRGB(0, 0, imageWidth, imageHeight, pixels, 0, imageWidth);

            ByteBuffer buffer = ByteBuffer.allocateDirect(imageWidth * imageHeight * 4);  

            for (int y = 0; y < imageHeight; y++) {
                for (int x = 0; x < imageWidth; x++) {
                    int pixel = pixels[y * imageWidth + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF)); 
                    buffer.put((byte) ((pixel >> 8) & 0xFF));  
                    buffer.put((byte) (pixel & 0xFF));         
                    buffer.put((byte) ((pixel >> 24) & 0xFF)); 
                }
            }
            buffer.flip();

            imData.buffer = buffer;

            return imData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public AssetType GetAssetType() {
        return AssetType.TEXTURE2D;
    }

    @Override
    public void OnDispose() {
        GL46.glDeleteTextures(this.textureId);
    }

    /**
     * Bindet Image zum benutzen auf den index index und diser kann dann beispielweise weiter an nen shader gegeben werden
     * 
     * @param index den index auf den die gebinded werden soll
     */
    public void Bind(int index) {
        GL46.glActiveTexture(GL46.GL_TEXTURE0 + index);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.textureId);
    }

    /**
     * Unbinded alle textures muss man ned machen auser man will sicher gehen sonst einfahc immer überschreiben
     */
    public void Unbind() {
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
    }

    /**
     * 
     * @return height in pxiel von dem image
     */
    public int GetHeight() {
        return this.height;
    }

    /**
     * 
     * @return width in pxiel von dem image
     */
    public int GetWidth() {
        return this.width;
    }

    @Override
    public String toString() {
        return new String("Texture with id: " + this.textureId);
    }

    private void ApplyParamters(TextureFiliteringMode filtering, TextureWrappingMode wrapping)
    {
        this.filiteringMode = filtering;
        this.wrappingMode = wrapping;

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

        CreateImageFromData(ByteBuffer.wrap(texture), textureWidth, textureHeight, filteringMode, wrapMode);
    }

    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        ImageData imData = GetImageDataFromPath(path);
        byte[] arr = new byte[imData.buffer.remaining()];

        imData.buffer.get(arr);

        stream.writeInt(width);
        stream.writeInt(height);

        stream.writeInt(this.wrappingMode.ordinal());
        stream.writeInt(this.filiteringMode.ordinal());

        stream.writeObject(arr);
    }
}
