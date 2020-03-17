package net.engine.picture;

import java.awt.*;

public abstract class Picture
{
  protected int width;
  protected int stride;
  protected int height;
  protected Object data;

  public Picture(int width, int height, int stride)
  {
    this.height = height;
    this.width = width;
    this.stride = stride;
    this.data = createData();
  }

  public void line(int x0, int y0, int x1, int y1, int colourIndex)
  {
    int dx = x1 - x0;
    int dy = y1 - y0;

    setPixel(x0, y0, colourIndex);
    if (Math.abs(dx) > Math.abs(dy))
    {
      float m = (float) dy / (float) dx;
      float b = y0 - m * x0;
      dx = (dx < 0) ? -1 : 1;
      while (x0 != x1)
      {
        x0 += dx;
        setPixel(x0, Math.round(m * x0 + b), colourIndex);
      }
    }
    else if (dy != 0)
    {
      float m = (float) dx / (float) dy;
      float b = x0 - m * y0;
      dy = (dy < 0) ? -1 : 1;
      while (y0 != y1)
      {
        y0 += dy;
        setPixel(Math.round(m * y0 + b), y0, colourIndex);
      }
    }
  }

  public void rectangle(int x0, int y0, int x1, int y1, int colourIndex)
  {
    for (int y = y0; y < y1; y++)
    {
      for (int x = x0; x < x1; x++)
      {
        setPixel(x, y, colourIndex);
      }
    }
  }

  public void circle(int xCenter, int yCenter, int radius, int colourIndex)
  {
    for (int x = -radius; x <= radius; x++)
    {
      int y = (int) Math.sqrt(radius * radius - x * x);
      line(x + xCenter, yCenter - y, x + xCenter, yCenter + y, colourIndex);
    }
  }

  public int getWidth()
  {
    return width;
  }

  public int getHeight()
  {
    return height;
  }

  public int getStride()
  {
    return stride;
  }

  public int IX(int x, int y)
  {
    return x + stride * y;
  }

  public void setPixel(int x, int y, int colourIndex)
  {
    if ((x >= 0) && (x < width))
    {
      if ((y >= 0) && (y < height))
      {
        setPixel(data, x, y, colourIndex);
      }
    }
  }

  public void unsafeSetPixel(int x, int y, int colourIndex)
  {
    setPixel(data, x, y, colourIndex);
  }

  public int getPixel(int x, int y)
  {
    if ((x >= 0) && (x < width))
    {
      if ((y >= 0) && (y < height))
      {
        return getPixel(data, x, y);
      }
    }
    return -1;
  }

  public int unsafeGetPixel(int x, int y)
  {
    return getPixel(data, x, y);
  }

  public Object getData()
  {
    return data;
  }

  public abstract Object createData();

  public abstract void setPixel(Object data, int x, int y, int colourIndex);

  public abstract int getPixel(Object data, int x, int y);

  public abstract void setPaletteColor(int index, Color color);

  public abstract void setPaletteFromColourGradient(Object... o);

  public abstract void clear(int colourIndex);
}

