package net.engine.shape;

import net.engine.math.Float2;
import net.engine.shape.result.DistanceResult;

public class LineSegment
    extends Shape
{
  public Float2 start;
  public Float2 direction;
  public float length;
  public Float2 end;

  public LineSegment(Float2 start)
  {
    this.length = 0;
    this.start = start;
    this.direction = null;
    this.end = start;
  }

  public LineSegment(Float2 start, Float2 direction, float length)
  {
    this.length = length;
    this.start = start;
    this.direction = direction;
    this.end = calculateEnd();
  }

  private Float2 calculateEnd()
  {
    return new Float2(this.start).add(new Float2(this.direction).multiply(this.length));
  }

  public boolean set(Float2 start, Float2 end)
  {
    this.start = start;
    direction = new Float2(end.x - start.x, end.y - start.y);
    if (!((direction.x == 0) && (direction.y == 0)))
    {
      length = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
      direction.x /= length;
      direction.y /= length;
      this.end = calculateEnd();
      return true;
    }
    else
    {
      this.length = 0;
      this.direction = null;
      this.end = start;
      return false;
    }
  }

  public float distanceFrom(Float2 point)
  {
    Float2 v = new Float2(point).subtract(start);
    return v.cross(direction);
  }

  public float distanceAlong(Float2 point)
  {
    Float2 v = new Float2(point).subtract(start);
    return v.dot(direction);
  }

  public DistanceResult distance(Float2 point)
  {
    Float2 v = new Float2(point).subtract(start);
    return new DistanceResult(v.cross(direction), v.dot(direction));
  }

  public DistanceResult distance(float x, float y)
  {
    Float2 v = new Float2(x - start.x, y - start.y);
    return new DistanceResult(v.cross(direction), v.dot(direction));
  }

  public Float2 center()
  {
    return new Float2(start).add(end).divide(2);
  }
}

