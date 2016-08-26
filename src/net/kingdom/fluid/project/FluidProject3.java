package net.kingdom.fluid.project;

import net.kingdom.fluid.FluidField;
import net.kingdom.FluidWork;

public class FluidProject3 extends FluidWork
{
  private FluidProjectParams params;
  private float halfNNegative;
  private int index;

  public FluidProject3(FluidField fluidField, FluidProjectParams params, float halfNNegative, int index)
  {
    super(fluidField);
    this.params = params;
    this.halfNNegative = halfNNegative;
    this.index = index;
  }

  @Override
  public void work()
  {
    fluidField.project3(params, halfNNegative, index);
  }
}

