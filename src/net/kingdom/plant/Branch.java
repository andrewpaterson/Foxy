package net.kingdom.plant;

import net.engine.math.Float2;
import net.engine.shape.Capsule;

public class Branch extends RaycastObject
{
  protected Capsule capsule;

  public Branch(Capsule capsule)
  {
    this.capsule = capsule;
  }

  @Override
  public boolean contains(Float2 position)
  {
    return capsule.contains(position);
  }

  @Override
  public void calculateBoundingBox()
  {
    bounding.set(capsule.getLeft(), capsule.getTop(), capsule.getRight(), capsule.getBottom());
  }
}

