package net.kingdom.plant;

import net.engine.shape.Rectangle;

public abstract class RaycastObject
{
  public Rectangle bounding;

  public RaycastObject()
  {
    bounding = new Rectangle();
  }

  public float getLeft()
  {
    return bounding.topLeft.x;
  }

  public float getRight()
  {
    return bounding.bottomRight.x;
  }

  public float getTop()
  {
    return bounding.topLeft.y;
  }

  public float getBottom()
  {
    return bounding.bottomRight.y;
  }

  public boolean isGroup()
  {
    return false;
  }

  public boolean probablyContains(float x, float y)
  {
    return bounding.contains(x, y);
  }

  public abstract RayResult cast(float x, float y);

  public abstract void calculateBoundingBox();
}

