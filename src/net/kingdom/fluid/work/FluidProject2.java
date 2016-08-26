package net.kingdom.fluid.work;

import net.kingdom.FluidWork;
import net.kingdom.fluid.FluidField;

public class FluidProject2 extends FluidWork
{
  private float[] sourceVelocityX;
  private float[] sourceVelocityY;
  private int index;

  public FluidProject2(FluidField fluidField, float[] sourceVelocityX, float[] sourceVelocityY, int y)
  {
    super(fluidField);
    this.sourceVelocityX = sourceVelocityX;
    this.sourceVelocityY = sourceVelocityY;
    this.index = fluidField.IX(1, y);
  }

  @Override
  public void work()
  {
    int offset = index;
    int width = fluidField.getWidth();
    for (int x = 1; x <= width; x++, offset++)
    {
      sourceVelocityX[offset] = 0.25f * (sourceVelocityY[offset] + fluidField.sumAdjacentValues(offset, sourceVelocityX));
    }
  }
}

