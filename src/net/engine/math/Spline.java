package net.engine.math;

public class Spline
{
  public Float2 start;
  public Float2 startControl;
  public Float2 endControl;
  public Float2 end;

  public Spline(Float2 start, Float2 startControl, Float2 endControl, Float2 end)
  {
    this.start = start;
    this.startControl = startControl;
    this.endControl = endControl;
    this.end = end;
  }

  public void generate(int steps, float[] x, float[] y)
  {
    steps--;
    for (int i = 0; i <= steps; i++)
    {
      float t = (float) i / (float) steps;
      x[i] = Bezier.n3(t, start.x, startControl.x, endControl.x, end.x);
      y[i] = Bezier.n3(t, start.y, startControl.y, endControl.y, end.y);
    }
  }
}

