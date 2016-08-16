package net.engine.global;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

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

  public static GraphicsEnvironment getGraphicsEnvironment()
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

  public static DisplayMode getDisplayMode(int width, int height)
  {
    GraphicsDevice graphicsDevice = getGraphicsEnvironment().getDefaultScreenDevice();

    int bitDepth = graphicsDevice.getDisplayMode().getBitDepth();
    int refreshRate = graphicsDevice.getDisplayMode().getRefreshRate();

    DisplayMode[] displayModes = graphicsDevice.getDisplayModes();

    for (DisplayMode displayMode : displayModes)
    {
      if (displayMode.getHeight() == height)
      {
        if (displayMode.getWidth() == width)
        {
          if (displayMode.getBitDepth() == bitDepth)
          {
            if (displayMode.getRefreshRate() == refreshRate)
            {
              return displayMode;
            }
          }
        }
      }
    }
    return null;
  }

  public static void fullScreen(Window window, int width, int height)
  {
    DisplayMode displayMode = GlobalGraphics.getDisplayMode(width, height);

    GraphicsDevice graphicsDevice = GlobalGraphics.getGraphicsEnvironment().getDefaultScreenDevice();
    graphicsDevice.setFullScreenWindow(window);
    graphicsDevice.setDisplayMode(displayMode);
  }

  public static BufferedImage convertFromIntArray(int width, int height, int[] data, BufferedImage image)
  {
    int colour[] = new int[4];
    WritableRaster raster = image.getWritableTile(0, 0);
    for (int y = 0; y < height; y++)
    {
      for (int x = 0; x < width; x++)
      {
        int color = data[y * width + x];
        colour[3] = (color & 0xff000000) >>> 24;
        colour[0] = color & 0xff;
        colour[1] = (color & 0xff00) >>> 8;
        colour[2] = (color & 0xff0000) >>> 16;
        raster.setPixel(x, y, colour);
      }
    }
    image.releaseWritableTile(0, 0);
    return image;
  }
}

