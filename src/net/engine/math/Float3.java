package net.engine.math;

/**
 *
 */
public class Float3
{
  public float x, y, z;

  public Float3()
  {
    this(0, 0, 0);
  }

  public Float3(float x, float y, float z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Float3(Float3 float3)
  {
    this.x = float3.x;
    this.y = float3.y;
    this.z = float3.z;
  }

  public Float3(Float4 float4)
  {
    this.x = float4.x;
    this.y = float4.y;
    this.z = float4.z;
  }

  public Float3 add(Float3 float3)
  {
    x += float3.x;
    y += float3.y;
    z += float3.z;
    return this;
  }

  public Float3 subtract(Float3 float3)
  {
    x -= float3.x;
    y -= float3.y;
    z -= float3.z;
    return this;
  }

  public Float3 multiply(float f)
  {
    x *= f;
    y *= f;
    z *= f;
    return this;
  }

  public Float3 divide(float f)
  {
    f = 1.0f / f;
    x *= f;
    y *= f;
    z *= f;
    return this;
  }

  public float squareMagnitude()
  {
    return x * x + y * y + z * z;
  }

  public float magnitude()
  {
    return (float) Math.sqrt(x * x + y * y + z * z);
  }

  public Float3 normalize()
  {
    return normalize(magnitude());
  }

  public Float3 normalize(float magnitude)
  {
    float f;

    f = magnitude;
    if (f != 0.0f)
    {
      f = 1.0f / f;
      x *= f;
      y *= f;
      z *= f;
    }
    return this;
  }

  public Float3 inverseForce()
  {
    float f;

    f = magnitude();
    if (f != 0.0f)
    {
      float fi = 1.0f / f;
      x = (x * fi) * fi;
      y = (y * fi) * fi;
      z = (z * fi) * fi;
    }
    return this;
  }

  public void inverse()
  {
    x = 1.0f / x;
    y = 1.0f / y;
    z = 1.0f / z;
  }
}

