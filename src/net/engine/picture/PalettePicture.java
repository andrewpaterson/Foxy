package net.engine.picture;

import java.awt.*;

public class PalettePicture
    extends Picture
{
  protected byte pixels[];
  protected Color[] palette;

  public PalettePicture(int width, int height)
  {
    super(width, height, width);
    pixels = (byte[]) data;

    for (int y = 0; y < height; y++)
    {
      for (int x = 0; x < width; x++)
      {
        pixels[IX(x, y)] = -128;
      }
    }

    palette = new Color[256];
  }

  @Override
  public Object createData()
  {
    return new byte[height * stride];
  }

  @Override
  public void setPixel(Object data, int x, int y, int colourIndex)
  {
    ((byte[]) data)[IX(x, y)] = toByte(colourIndex);
  }

  @Override
  public int getPixel(Object data, int x, int y)
  {
    return fromByte(((byte[]) data)[IX(x, y)]);
  }

  private byte toByte(int colourIndex)
  {
    return (byte) (colourIndex - 128);
  }

  private int fromByte(byte b)
  {
    return 128 + b;
  }

  public Color getPaletteColour(int index)
  {
    return palette[index];
  }

  @Override
  public void setPaletteColor(int index, Color color)
  {
    palette[index] = color;
  }

  @Override
  public void setPaletteFromColourGradient(Object... o)
  {
    ColourGradient.generate(palette, o);
  }
}

