package net.kingdom.plant;

import net.engine.shape.Capsule;

public class Branch extends RaycastObject
{
  protected Capsule capsule;

  public Branch(Capsule capsule)
  {
    this.capsule = capsule;
  }

  @Override
  public boolean contains(float x, float y)
  {
    return capsule.contains(x, y);
  }

  @Override
  public void calculateBoundingBox()
  {
    bounding.set(capsule.getLeft(), capsule.getTop(), capsule.getRight(), capsule.getBottom());
  }
}

