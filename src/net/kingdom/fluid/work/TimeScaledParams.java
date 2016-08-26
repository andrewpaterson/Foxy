package net.kingdom.fluid.work;

public class TimeScaledParams
{
  public float[] destination;
  public float[] source;
  public float timeStep;
  public int stride;

  public TimeScaledParams(float[] destination, float[] source, int stride, float timeStep)
  {
    this.destination = destination;
    this.source = source;
    this.stride = stride;
    this.timeStep = timeStep;
  }
}

