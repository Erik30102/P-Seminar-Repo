package com.Pseminar.Graphics.Buffers;

import java.awt.image.BufferedImage;
import java.awt.Color;

// TODO: legit nix mit nem opengl frambuffer zu tun
// https://www.khronos.org/opengl/wiki/Framebuffer_Object wenn wer implementiren will

public class FrameBuffer {
    private int width;
    private int height;
    private BufferedImage buffer;

    public FrameBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * Sets the color of a specific pixel in the framebuffer.
     * @param x The x coordinate of the pixel.
     * @param y The y coordinate of the pixel.
     * @param color The color to set the pixel.
     */
    public void setPixel(int x, int y, Color color) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            buffer.setRGB(x, y, color.getRGB());
        }
    }

    /**
     * Clears the framebuffer with a specified color.
     * @param color The color to clear the framebuffer with.
     */
    public void clear(Color color) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                buffer.setRGB(x, y, color.getRGB());
            }
        }
    }

    /**
     * Returns the BufferedImage representing the framebuffer.
     * @return The BufferedImage of the framebuffer.
     */
    public BufferedImage getBuffer() {
        return buffer;
    }
}
