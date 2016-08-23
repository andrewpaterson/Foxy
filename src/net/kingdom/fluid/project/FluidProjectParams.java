package net.kingdom.fluid.project;

/**
 * Created by andrew on 2016/08/23.
 */
public class FluidProjectParams
{
  public float[] destinationVelocityX;
  public float[] destinationVelocityY;
  public float[] sourceVelocityX;
  public float[] sourceVelocityY;

  public FluidProjectParams(float[] destinationVelocityX, float[] destinationVelocityY, float[] sourceVelocityX, float[] sourceVelocityY)
  {
    this.destinationVelocityX = destinationVelocityX;
    this.destinationVelocityY = destinationVelocityY;
    this.sourceVelocityX = sourceVelocityX;
    this.sourceVelocityY = sourceVelocityY;
  }
}
