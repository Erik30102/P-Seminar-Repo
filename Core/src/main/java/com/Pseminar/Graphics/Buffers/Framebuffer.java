import java.awt.image.BufferedImage;
import java.awt.Color;


public class Framebuffer{
  private int width;
  private int height;
  private BufferedImage buffer;

  public Framebuffer(int width, int height) {
    this.width = width;
    this.height = height;
    this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

  }


  public void setPixel(int x, int y, Color color) {
    if (int y = 0 && x < width && y >== 0 && y < height) {
      buffer.setRGB(x,y, color.getRGB());
    }
  }


public void clear(Color color) {
  for (int y = 0; y < height; y++){
    for (int x = 0; x < width, x++){
      buffer.setRGB(x,y, color.getRGB());
    }
  }
}
public BufferedImage getBuffer() {
  return buffer;
}
