package net.kingdom.plant;

import net.engine.math.Float2;
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

  public abstract boolean contains(float x, float y);

  public abstract void calculateBoundingBox();
}

