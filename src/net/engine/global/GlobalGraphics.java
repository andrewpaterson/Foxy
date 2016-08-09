package net.engine.global;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GlobalGraphics
{
  private static GraphicsEnvironment localGraphicsEnvironment;
  private static GraphicsConfiguration graphicsConfiguration;

  public static GraphicsConfiguration getGraphicsConfiguration()
  {
    GraphicsEnvironment graphicsEnvironment = getGraphicsEnvironment();
    if (graphicsConfiguration == null)
    {
      graphicsConfiguration = graphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration();
    }
    return graphicsConfiguration;
  }

  private static GraphicsEnvironment getGraphicsEnvironment()
  {
    if (localGraphicsEnvironment == null)
    {
      localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    }
    return localGraphicsEnvironment;
  }

  public static BufferedImage convertToBufferedImage(Image image, int dx, int dy, int sx, int sy, int width, int height, int transparency)
  {
    GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();

    BufferedImage copy = graphicsConfiguration.createCompatibleImage(width, height, transparency);
    Graphics2D g2d = copy.createGraphics();
    g2d.drawImage(image, dx, dy, dx + width, dy + height, sx, sy, sx + width, sy + height, null);
    g2d.dispose();
    return copy;
  }
}

