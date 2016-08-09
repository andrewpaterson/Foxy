package net.engine.cel;

import net.engine.math.Float2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class CelTrimmer
{
  public CelTrimmer()
  {
  }

  public Cel trim(BufferedImage bufferedImage)
  {
    WritableRaster raster = bufferedImage.getRaster();

    int top;
    for (top = 0; top < raster.getHeight(); top++)
    {
      if (!isRowTransparent(raster, 0, raster.getWidth(), top))
      {
        break;
      }
    }

    if (top == raster.getHeight())
    {
      return null;
    }

    int bottom;
    for (bottom = raster.getHeight() - 1; bottom >= 0; bottom--)
    {
      if (!isRowTransparent(raster, 0, raster.getWidth(), bottom))
      {
        break;
      }
    }

    int left;
    for (left = 0; left < raster.getWidth(); left++)
    {
      if (!isColumnTransparent(raster, left, top, bottom + 1))
      {
        break;
      }
    }

    int right;
    for (right = raster.getWidth() - 1; right >= 0; right--)
    {
      if (!isColumnTransparent(raster, right, top, bottom + 1))
      {
        break;
      }
    }

    int width = right - left + 1;
    int height = bottom - top + 1;

    right = raster.getWidth() - (right + 1);
    bottom = raster.getHeight() - (bottom + 1);

    if ((top == 0) && (bottom == 0) && (left == 0) && (right == 0))
    {
      return new Cel(bufferedImage, Cel.CENTERED, Cel.CENTERED, new Float2(0, 0), new Float2(0, 0));
    }
    else
    {
      bufferedImage = convertToBufferedImage(bufferedImage, 0, 0, left, top, width, height, Transparency.TRANSLUCENT);
      return new Cel(bufferedImage, Cel.CENTERED, Cel.CENTERED, new Float2(left, top), new Float2(right, bottom));
    }
  }

  private boolean isRowTransparent(WritableRaster raster, int x1, int x2, int y)
  {
    for (int x = x1; x < x2; x++)
    {
      if (!isTransparent(raster, x, y))
      {
        return false;
      }
    }
    return true;
  }

  private boolean isColumnTransparent(WritableRaster raster, int x, int y1, int y2)
  {
    for (int y = y1; y < y2; y++)
    {
      if (!isTransparent(raster, x, y))
      {
        return false;
      }
    }
    return true;
  }

  private boolean isTransparent(WritableRaster raster, int x, int y)
  {
    int colour[] = new int[4];
    raster.getPixel(x, y, colour);
    return colour[3] == 0;
  }


  private BufferedImage convertToBufferedImage(Image image, int dx, int dy, int sx, int sy, int width, int height, int transparency)
  {
    GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();

    BufferedImage copy = graphicsConfiguration.createCompatibleImage(width, height, transparency);
    Graphics2D g2d = copy.createGraphics();
    g2d.drawImage(image, dx, dy, dx + width, dy + height, sx, sy, sx + width, sy + height, null);
    g2d.dispose();
    return copy;
  }

  private GraphicsConfiguration getGraphicsConfiguration()
  {
    GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    return graphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration();
  }
}

