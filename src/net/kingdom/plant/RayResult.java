package net.kingdom.plant;

import net.engine.math.Float3;

public class RayResult
{
  public float z;
  public Float3 normal;

  public RayResult(float z, Float3 normal)
  {
    this.z = z;
    this.normal = normal;
  }
}

