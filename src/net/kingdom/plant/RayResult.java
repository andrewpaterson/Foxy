package net.kingdom.plant;

import net.engine.math.Float3;

import java.awt.*;

public class RayResult
{
  public float z;
  public Float3 normal;
  public Color color;

  public RayResult(float z, Float3 normal, Color color)
  {
    this.z = z;
    this.normal = normal;
    this.color = color;
  }
}

