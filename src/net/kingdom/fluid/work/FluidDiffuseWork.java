package net.kingdom.fluid.work;

import net.kingdom.FluidWork;
import net.kingdom.fluid.FluidField;

public class FluidDiffuseWork extends FluidWork
{
  private float[] destination;
  private float[] source;
  private float a;
  private float constant;
  private int index;

  public FluidDiffuseWork(FluidField fluidField, float[] destination, float[] source, float a, float constant, int y)
  {
    super(fluidField);
    this.destination = destination;
    this.source = source;
    this.a = a;
    this.constant = constant;
    this.index = fluidField.IX(1, y);
  }

  @Override
  public void work()
  {
    int offset = index;
    int width = getWidth();

    for (int x = 1; x <= width; x++, offset++)
    {
      float adjacentValueSum = fluidField.sumAdjacentValues(offset, source);
      destination[offset] = constant * (source[offset] + a * adjacentValueSum);
    }
  }
}

