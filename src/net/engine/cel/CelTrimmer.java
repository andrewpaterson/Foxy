package net.engine.cel;

import net.engine.global.GlobalGraphics;
import net.engine.math.Int2;

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
      return new Cel(bufferedImage, Cel.CENTERED, Cel.CENTERED, new Int2(0, 0), new Int2(0, 0));
    }
    else
    {
      bufferedImage = GlobalGraphics.convertToBufferedImage(bufferedImage, 0, 0, left, top, width, height, Transparency.TRANSLUCENT);
      return new Cel(bufferedImage, Cel.CENTERED, Cel.CENTERED, new Int2(left, top), new Int2(right, bottom));
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
    int[] colour = new int[4];
    raster.getPixel(x, y, colour);
    return colour[3] == 0;
  }
}

