package com.Pseminar.TextureLoader;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;

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
    public static int createTexture(String path) {
        ByteBuffer buffer = loadTexture(path);
        
        if (buffer == null) {
            System.err.println("Failed to load texture: " + path);
            return -1;
        }

        int width = imageWidth;
        int height = imageHeight;

        int textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0,
                          GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        System.out.println("Texture loaded successfully with ID: " + textureID);
        return textureID;
    }
}

