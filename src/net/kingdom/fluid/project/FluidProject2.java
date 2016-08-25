package net.kingdom.fluid.project;

import net.kingdom.fluid.FluidField;
import net.kingdom.FluidWork;

/**
 * Created by andrew on 2016/08/23.
 */
public class FluidProject2 extends FluidWork
{
  private FluidProjectParams params;
  private int index;

  public FluidProject2(FluidField fluidField, FluidProjectParams params, int index)
  {
    super(fluidField);
    this.params = params;
    this.index = index;
  }

  @Override
  public int work()
  {
    fluidField.project2(params, index);
    return SUCCESS;
  }
}

