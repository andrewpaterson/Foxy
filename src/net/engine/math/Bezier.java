package net.engine.math;

/**
 * Created by andrew on 2016/09/03.
 */
public class Bezier
{
  public static float n2(float t, float w1, float w2, float w3)
  {
    float t2 = t * t;
    float mt = 1 - t;
    float mt2 = mt * mt;
    return w1 * mt2 + w2 * 2 * mt * t + w3 * t2;
  }

  public static float n3(float t, float w1, float w2, float w3, float w4)
  {
    float t2 = t * t;
    float t3 = t2 * t;
    float mt = 1 - t;
    float mt2 = mt * mt;
    float mt3 = mt2 * mt;
    return w1 * mt3 + 3 * w2 * mt2 * t + 3 * w3 * mt * t2 + w4 * t3;
  }
}

