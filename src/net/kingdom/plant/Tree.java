package net.kingdom.plant;

import net.engine.global.GlobalRandom;
import net.engine.math.Float2;
import net.engine.shape.Capsule;

import java.util.ArrayList;
import java.util.List;

public class Tree extends RaycastGroup
{
  public List<Branch> branches;

  public Tree(Float2 position)
  {
    super();
    branches = new ArrayList<>();
    Float2 start = position;
    int steps = 10;
    float radius = steps + 1;

    for (int i = 0; i < steps; i++)
    {
      int ax = GlobalRandom.random.nextInt(21) - 10;
      int ay = GlobalRandom.random.nextInt(21) - 10;

      Float2 end = new Float2(start.x + ax, start.y - 20 + ay);
      Capsule capsule = new Capsule(start, radius, end, radius - 1);
      start = end;
      radius--;

      branches.add(new Branch(capsule));
    }
  }

  public boolean contains(float x, float y)
  {
    if (bounding.contains(x, y))
    {
      List<? extends RaycastObject> objects = getObjects();
      for (RaycastObject raycastObject : objects)
      {
        if (raycastObject.contains(x, y))
        {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public List<? extends RaycastObject> getObjects()
  {
    return branches;
  }
}

