package net.kingdom.fluid.work;

import net.kingdom.fluid.FluidField;

public class TimeScaledWork extends FluidWork
{
  private float[] destination;
  private float[] source;
  private int index;

  public TimeScaledWork(FluidField fluidField, float[] destination, float[] source, int y)
  {
    super(fluidField);
    this.index = y * getStride();
    this.destination = destination;
    this.source = source;
  }

  @Override
  public void work()
  {
    int stride = getStride();
    float timeStep = getTimeStep();
    for (int x = 0; x < stride; x++)
    {
      destination[index + x] += timeStep * source[index + x];
    }
  }
}

