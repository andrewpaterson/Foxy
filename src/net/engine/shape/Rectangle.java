package net.engine.shape;

import net.engine.math.Float2;

public class Rectangle
    extends Shape
{
  public Float2 topLeft;
  public Float2 bottomRight;

  public Rectangle()
  {
    topLeft = new Float2();
    bottomRight = new Float2();
  }

  public Rectangle(Float2 topLeft, Float2 bottomRight)
  {
    this.topLeft = new Float2(topLeft);
    this.bottomRight = new Float2(bottomRight);
  }

  public void set(Float2 topLeft, Float2 bottomRight)
  {
    this.topLeft.x = topLeft.x;
    this.topLeft.y = topLeft.y;
    this.bottomRight.x = bottomRight.x;
    this.bottomRight.y = bottomRight.y;
  }

  public void growToContainX(float x)
  {
    if (x < topLeft.x)
    {
      topLeft.x = x;
    }
    else if (x > bottomRight.x)
    {
      bottomRight.x = x;
    }
  }

  public void growToContainY(float y)
  {
    if (y < topLeft.y)
    {
      topLeft.y = y;
    }
    else if (y > bottomRight.y)
    {
      bottomRight.y = y;
    }
  }

  public Float2 getTopLeft()
  {
    return topLeft;
  }

  public Float2 getBottomRight()
  {
    return bottomRight;
  }

  public boolean contains(Float2 position)
  {
    return contains(position.x, position.y);
  }

  public boolean contains(float x, float y)
  {
    if ((x < topLeft.x) || (x > bottomRight.x))
    {
      return false;
    }
    return !((y < topLeft.y) || (y > bottomRight.y));
  }

  public void set(float left, float top, float right, float bottom)
  {
    topLeft.x = left;
    topLeft.y = top;

    bottomRight.x = right;
    bottomRight.y = bottom;
  }
}

