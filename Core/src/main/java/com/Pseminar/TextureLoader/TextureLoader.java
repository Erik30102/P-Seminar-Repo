package com.Pseminar.TextureLoader;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL46;

public class TextureLoader {

    private static int imageWidth;
    private static int imageHeight;

    // load image and convert to ByteBuffer
    public static ByteBuffer loadTexture(String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();

            int[] pixels = new int[imageWidth * imageHeight];
            image.getRGB(0, 0, imageWidth, imageHeight, pixels, 0, imageWidth);

            ByteBuffer buffer = ByteBuffer.allocateDirect(imageWidth * imageHeight * 4);  

            // Loop each pixel and add to the buffer
            for (int y = 0; y < imageHeight; y++) {
                for (int x = 0; x < imageWidth; x++) {
                    int pixel = pixels[y * imageWidth + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
                    buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green
                    buffer.put((byte) (pixel & 0xFF));         // Blue
                    buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
                }
            }
            buffer.flip();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to create an OpenGL texture from the ByteBuffer
    public static int createTexture(ByteBuffer buffer) {        
        int width = imageWidth;
        int height = imageHeight;

        int textureID = GL46.glGenTextures();
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, textureID);

        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_RGBA, width, height, 0,
                          GL46.GL_RGBA, GL46.GL_UNSIGNED_BYTE, buffer);

        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_S, GL46.GL_REPEAT);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_WRAP_T, GL46.GL_REPEAT);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_NEAREST);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST);

        System.out.println("Texture loaded successfully with ID: " + textureID);
        return textureID;
    }
}

