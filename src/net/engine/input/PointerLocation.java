package net.engine.input;

public class PointerLocation
{
  private int x;
  private int y;
  private int sx;
  private int sy;

  public PointerLocation(int x, int y, int sx, int sy)
  {
    this.x = x;
    this.y = y;
    this.sx = sx;
    this.sy = sy;
  }

  public int getX()
  {
    return x;
  }

  public int getY()
  {
    return y;
  }

  public int getScreenX()
  {
    return sx;
  }

  public int getScreenY()
  {
    return sy;
  }
}

