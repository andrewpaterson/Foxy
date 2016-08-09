package net.engine.math;

/**
 * Created by andrew on 2016/08/09.
 */
public class Int2
{
  public int x;
  public int y;

  public Int2()
  {
    this.x = 0;
    this.y = 0;
  }

  public Int2(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public Int2(Int2 int2)
  {
    x = int2.x;
    y = int2.y;
  }

  public Int2 add(Int2 int2)
  {
    x += int2.x;
    y += int2.y;
    return this;
  }

  public Int2 subtract(Int2 int2)
  {
    x -= int2.x;
    y -= int2.y;
    return this;
  }

  public Int2 multiply(int f)
  {
    x *= f;
    y *= f;
    return this;
  }

  public Int2 set(int x, int y)
  {
    this.x = x;
    this.y = y;
    return this;
  }

  public int magnitude()
  {
    return (int) Math.sqrt(x * x + y * y);
  }

  public int squareMagnitude()
  {
    return x * x + y * y;
  }

  public int dot(Int2 int2)
  {
    return x * int2.x + y * int2.y;
  }

  public int cross(Int2 int2)
  {
    return x * int2.y - y * int2.x;
  }

  public String toString()
  {
    return x + ", " + y;
  }

  public boolean isZero()
  {
    return (x == 0.0f) && (y == 0.0f);
  }
}

