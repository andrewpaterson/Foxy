package net.kingdom.fluid.work;

import net.kingdom.fluid.FluidField;

public class FluidProject1 extends FluidWork
{
  private float[] destinationVelocityX;
  private float[] destinationVelocityY;
  private float[] sourceVelocityY;
  private float halfHNegative;
  private int index;

  public FluidProject1(FluidField fluidField, float[] destinationVelocityX, float[] destinationVelocityY, float[] sourceVelocityY, float halfHNegative, int y)
  {
    super(fluidField);
    this.destinationVelocityX = destinationVelocityX;
    this.destinationVelocityY = destinationVelocityY;
    this.sourceVelocityY = sourceVelocityY;
    this.halfHNegative = halfHNegative;
    this.index = fluidField.IX(1, y);
  }

  @Override
  public void work()
  {
    int offset = index;
    int width = fluidField.getWidth();
    int stride = fluidField.getStride();

    for (int x = 1; x <= width; x++, offset++)
    {
      sourceVelocityY[offset] = halfHNegative * (destinationVelocityX[offset + 1] - destinationVelocityX[offset - 1] + destinationVelocityY[offset + stride] - destinationVelocityY[offset - stride]);
    }
  }
}

