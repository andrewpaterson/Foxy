package net.kingdom.fluid.work;

import net.kingdom.FluidWork;
import net.kingdom.fluid.FluidField;

public class TimeScaledWork extends FluidWork
{
  private float[] destination;
  private float[] source;
  private float timeStep;
  private int index;

  public TimeScaledWork(FluidField fluidField, float[] destination, float[] source, float timeStep, int index)
  {
    super(fluidField);
    this.index = index;
    this.destination = destination;
    this.source = source;
    this.timeStep = timeStep;
  }

  @Override
  public void work()
  {
    int stride = getStride();
    for (int x = 0; x < stride; x++)
    {
      destination[index + x] += timeStep * source[index + x];
    }
  }
}

