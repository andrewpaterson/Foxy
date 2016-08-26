package net.kingdom.fluid.work;

/**
 * Created by andrew on 2016/08/23.
 */
public class FluidDiffuseParams
{
  public float[] destination;
  public float[] source;
  public float a;

  public FluidDiffuseParams(float[] destination, float[] source, float a)
  {
    this.destination = destination;
    this.source = source;
    this.a = a;
  }
}

