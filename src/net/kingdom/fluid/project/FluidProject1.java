package net.kingdom.fluid.project;

import net.kingdom.FluidWork;
import net.kingdom.fluid.FluidField;

/**
 * Created by andrew on 2016/08/23.
 */
public class FluidProject1 extends FluidWork
{
  private FluidProjectParams params;
  private float halfHNegative;
  private int index;

  public FluidProject1(FluidField fluidField, FluidProjectParams params, float halfHNegative, int index)
  {
    super(fluidField);
    this.params = params;
    this.halfHNegative = halfHNegative;
    this.index = index;
  }

  @Override
  public void work()
  {
    fluidField.project1(params, halfHNegative, index);
  }
}

