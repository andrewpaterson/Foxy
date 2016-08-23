package net.kingdom.fluid.advect;

/**
 * Created by andrew on 2016/08/23.
 */
public class FluidAdvectParams
{
  public float[] density;
  public float[] densityPrevious;
  public float[] velocityX;
  public float[] velocityY;
  public float dt;

  public FluidAdvectParams(float[] density, float[] densityPrevious, float[] velocityX, float[] velocityY, float dt)
  {
    this.density = density;
    this.densityPrevious = densityPrevious;
    this.velocityX = velocityX;
    this.velocityY = velocityY;
    this.dt = dt;
  }
}

