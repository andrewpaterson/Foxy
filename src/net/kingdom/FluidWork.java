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

  public int IX(int x, int y)
  {
    return fluidField.IX(x, y);
  }

  protected int getStride()
  {
    return fluidField.getStride();
  }

  protected int getHeight()
  {
    return fluidField.getHeight();
  }

  protected int getWidth()
  {
    return fluidField.getWidth();
  }

  protected float getTimeStep()
  {
    return fluidField.getTimeStep();
  }
}

