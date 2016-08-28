package net.engine.picture;

import java.awt.*;

public class ComponentPicture extends BasePicture
{
  protected int pixels[];

  public ComponentPicture(int width, int height)
  {
    super(width, height, width);
    pixels = (int[]) data;

    for (int y = 0; y < height; y++)
    {
      for (int x = 0; x < width; x++)
      {
        pixels[IX(x, y)] = -128;
      }
    }
  }

  @Override
  protected Object createData()
  {
    return new int[height * stride];
  }

  @Override
  protected void setPixel(Object data, int x, int y, int colour)
  {
    ((int[]) data)[IX(x, y)] = colour;
  }

  @Override
  protected int getPixel(Object data, int x, int y)
  {
    return ((int[]) data)[IX(x, y)];
  }

  @Override
  public void setPaletteColor(int index, Color color)
  {
  }

  @Override
  public void setPaletteFromColourGradient(Object... o)
  {
  }
}

