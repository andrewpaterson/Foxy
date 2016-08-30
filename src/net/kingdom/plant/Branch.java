package net.kingdom.plant;

import net.engine.math.Float3;
import net.engine.shape.Capsule;
import net.engine.shape.Circle;
import net.engine.shape.LineSegment;
import net.engine.shape.result.DistanceResult;

public class Branch extends RaycastObject
{
  protected Capsule capsule;

  public Branch(Capsule capsule)
  {
    this.capsule = capsule;
  }

  @Override
  public void calculateBoundingBox()
  {
    bounding.set(capsule.getLeft(), capsule.getTop(), capsule.getRight(), capsule.getBottom());
  }

  public RayResult cast(float x, float y)
  {
    if (probablyContains(x, y))
    {
      Float3 float3 = surfaceNormal(x, y);
      if (float3 != null)
      {
        return new RayResult(-float3.z, float3);
      }
      else
      {
        return null;
      }
    }
    else
    {
      return null;
    }
  }

  public Float3 surfaceNormal(float x, float y)
  {
    LineSegment line = capsule.getLine();
    Circle start = capsule.getStart();
    Circle end = capsule.getEnd();

    DistanceResult distance = line.distance(x, y);

    if ((distance.along >= 0) && (distance.along <= line.length))
    {
      float normalisedAlong = distance.along / line.length;
      float radius = start.radius * (1 - normalisedAlong) + end.getRadius() * normalisedAlong;
      if ((distance.from <= radius) && (distance.from >= -radius))
      {
        Float3 surfaceNormal;
        if (distance.from >= 0)
        {
          float normalisedFrom = distance.from / radius;
          surfaceNormal = new Float3(normalisedFrom * line.direction.y, normalisedFrom * -line.direction.x, 1.0f - normalisedFrom);
        }
        else
        {
          float normalisedFrom = (-distance.from) / radius;
          surfaceNormal = new Float3(normalisedFrom * line.direction.y, normalisedFrom * line.direction.x, 1.0f - normalisedFrom);
        }
        surfaceNormal.normalize();
        return surfaceNormal;
      }
    }

    if (start.contains(x, y))
    {
      return sphereNormal(x, y, start);
    }
    if (end.contains(x, y))
    {
      return sphereNormal(x, y, end);
    }
    return null;
  }

  private Float3 sphereNormal(float x, float y, Circle start)
  {
    float dx = start.center.x - x;
    float dy = start.center.y - y;
    float radius = start.radius;
    float distanceFrom = (float) (Math.sqrt(dx * dx + dy * dy));
    float normalisedDistance = distanceFrom / radius;
    Float3 surfaceNormal = new Float3(dx / radius, dy / radius, 1.0f - normalisedDistance);
    surfaceNormal.normalize();
    return surfaceNormal;
  }
}

