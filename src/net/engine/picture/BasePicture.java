package net.engine.picture;

public abstract class BasePicture
{
  protected int width;
  protected int stride;
  protected int height;

  public BasePicture(int width, int height, int stride)
  {
    this.stride = width;
    this.height = height;
    this.width = width;
  }

  public abstract void setPixel(int x, int y, int colourIndex);

  public abstract int getPixel(int x, int y);

  public abstract void line(int x0, int y0, int x1, int y1, int colourIndex);

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

  public abstract void circle(int xCenter, int yCenter, int radius, int colourIndex);

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
}
