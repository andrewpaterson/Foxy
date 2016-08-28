package net.engine.shape;

import net.engine.math.Float2;

public class Circle extends Shape
{
  public Float2 center;
  public float radius;

  public Circle(Float2 center, float radius)
  {
    this.center = center;
    this.radius = radius;
  }

  public Float2 getAbsoluteCenter()
  {
    return center;
  }

  public float getRadius()
  {
    return radius;
  }

  public boolean contains(Float2 point)
  {
    return circleContains(point.x, point.y, center.x, center.y, radius);
  }

  @SuppressWarnings("SimplifiableIfStatement")
  public static boolean circleContains(float px, float py, float cx, float cy, float radius)
  {
    float dx = Math.abs(px - cx);
    float dy = Math.abs(py - cy);
    if ((dx > radius) || (dy > radius))
    {
      return false;
    }
    if (dx + dy <= radius)
    {
      return true;
    }
    return (dx * dx + dy * dy) <= (radius * radius);
  }
}

