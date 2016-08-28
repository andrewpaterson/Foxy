package net.engine.shape;

import net.engine.math.Float2;

public class Capsule
{
  protected LineSegment line;
  protected Circle start;  //Bottom
  protected Circle end;  //Top

  public Capsule(Float2 start, float startRadius, Float2 end, float endRadius)
  {
    line = new LineSegment(start);
    line.set(start, end);
    this.start = new Circle(start, startRadius);
    this.end = new Circle(end, endRadius);
  }

  public Float2 getStart()
  {
    return start.center;
  }

  public Float2 getEnd()
  {
    return end.center;
  }
}
