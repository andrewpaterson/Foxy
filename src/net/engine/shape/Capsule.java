package net.engine.shape;

import net.engine.math.Float2;
import net.engine.shape.result.DistanceResult;

public class Capsule
{
  protected LineSegment line;
  protected Circle start;  //Bottom
  protected Circle end;  //Top

  protected Circle bounding;

  public Capsule(Float2 start, float startRadius, Float2 end, float endRadius)
  {
    line = new LineSegment(start);
    line.set(start, end);
    this.start = new Circle(start, startRadius);
    this.end = new Circle(end, endRadius);

    float radius = line.length / 2;
    radius = startRadius > endRadius ? radius + startRadius : radius + endRadius;
    bounding = new Circle(line.center(), radius);
  }

  public Float2 getStart()
  {
    return start.center;
  }

  public Float2 getEnd()
  {
    return end.center;
  }

  @SuppressWarnings("RedundantIfStatement")
  public boolean contains(Float2 point)
  {
    if (!bounding.contains(point))
    {
      return false;
    }

    DistanceResult distance = line.distance(point);

    if ((distance.along >= 0) && (distance.along <= line.length))
    {
      float fraction = distance.along / line.length;
      float radius = start.getRadius() * (1 - fraction) + end.getRadius() * fraction;
      float distanceFrom = Math.abs(distance.from);
      if (distanceFrom <= radius)
      {
        return true;
      }
    }

    if (start.contains(point))
    {
      return true;
    }
    if (end.contains(point))
    {
      return true;
    }
    return false;
  }

  public float getLeft()
  {
    float startLeft = start.center.x - start.radius;
    float endLeft = end.center.x - end.radius;
    return startLeft < endLeft ? startLeft : endLeft;
  }

  public float getRight()
  {
    float startRight = start.center.x + start.radius;
    float endRight = end.center.x + end.radius;
    return startRight > endRight ? startRight : endRight;
  }

  public float getTop()
  {
    float startTop = start.center.y - start.radius;
    float endTop = end.center.y - end.radius;
    return startTop < endTop ? startTop : endTop;
  }

  public float getBottom()
  {
    float startBottom = start.center.y + start.radius;
    float endBottom = end.center.y + end.radius;
    return startBottom > endBottom ? startBottom : endBottom;
  }
}

