package net.kingdom.plant;

import net.engine.common.EngineException;

import java.util.List;

public abstract class RaycastGroup extends RaycastObject
{
  public RaycastGroup()
  {
  }

  public void calculateBoundingBox()
  {
    List<? extends RaycastObject> objects = getObjects();
    for (RaycastObject raycastObject : objects)
    {
      raycastObject.calculateBoundingBox();
    }

    if (objects.size() > 0)
    {
      RaycastObject raycastObject = objects.get(0);
      bounding.set(raycastObject.getLeft(), raycastObject.getTop(), raycastObject.getRight(), raycastObject.getBottom());

      for (int i = 1; i < objects.size(); i++)
      {
        raycastObject = objects.get(i);
        bounding.growToContainX(raycastObject.getLeft());
        bounding.growToContainX(raycastObject.getRight());

        bounding.growToContainY(raycastObject.getTop());
        bounding.growToContainY(raycastObject.getBottom());
      }
    }
  }

  @Override
  public boolean isGroup()
  {
    return true;
  }

  @Override
  public RayResult cast(float x, float y)
  {
    throw new EngineException("Can't call cast on Group objects");
  }

  public abstract List<? extends RaycastObject> getObjects();
}

