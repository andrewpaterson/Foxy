package net.kingdom.fluid.work;

import net.kingdom.fluid.FluidField;

public class FluidProject3 extends FluidWork
{
  private float[] destinationVelocityX;
  private float[] destinationVelocityY;
  private float[] sourceVelocityX;
  private float halfNNegative;
  private int index;

  public FluidProject3(FluidField fluidField, float[] destinationVelocityX, float[] destinationVelocityY, float[] sourceVelocityX, float halfNNegative, int y)
  {
    super(fluidField);
    this.destinationVelocityX = destinationVelocityX;
    this.destinationVelocityY = destinationVelocityY;
    this.sourceVelocityX = sourceVelocityX;
    this.halfNNegative = halfNNegative;
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
      destinationVelocityX[offset] += halfNNegative * (sourceVelocityX[offset + 1] - sourceVelocityX[offset - 1]);
      destinationVelocityY[offset] += halfNNegative * (sourceVelocityX[offset + stride] - sourceVelocityX[offset - stride]);
    }
  }
}

