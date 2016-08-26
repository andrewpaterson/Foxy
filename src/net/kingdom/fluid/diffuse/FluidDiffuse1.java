package net.kingdom.fluid.diffuse;

import net.kingdom.fluid.FluidField;
import net.kingdom.FluidWork;

/**
 * Created by andrew on 2016/08/23.
 */
public class FluidDiffuse1 extends FluidWork
{
  private FluidDiffuseParams params;
  private float constant;
  private int index;

  public FluidDiffuse1(FluidField fluidField, FluidDiffuseParams params, float constant, int index)
  {
    super(fluidField);
    this.params = params;
    this.constant = constant;
    this.index = index;
  }

  @Override
  public void work()
  {
    fluidField.diffuse1(params, constant, index);
  }
}

