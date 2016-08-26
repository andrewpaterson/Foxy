package net.kingdom.fluid.work;

import net.kingdom.FluidWork;
import net.kingdom.fluid.FluidField;

/**
 * Created by andrew on 2016/08/26.
 */
public class TimeScaledWork extends FluidWork
{
  private TimeScaledParams params;
  private int index;

  public TimeScaledWork(FluidField fluidField, TimeScaledParams params, int index)
  {
    super(fluidField);
    this.params = params;
    this.index = index;
  }

  @Override
  public void work()
  {
    for (int x = 0; x < params.stride; x++)
    {
      params.destination[index + x] += params.timeStep * params.source[index + x];
    }
  }
}

