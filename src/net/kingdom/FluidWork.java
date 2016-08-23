package net.kingdom;

import net.engine.thread.Work;
import net.kingdom.fluid.FluidField;

public abstract class FluidWork extends Work
{
  protected FluidField fluidField;

  public FluidWork(FluidField fluidField)
  {
    this.fluidField = fluidField;
  }
}