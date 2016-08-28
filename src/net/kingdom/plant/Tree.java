package net.kingdom.plant;

import net.engine.global.GlobalRandom;
import net.engine.math.Float2;
import net.engine.shape.Capsule;
import net.engine.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Tree
{
  public Float2 position;
  public List<Capsule> branches;

  public Rectangle bounding;

  public Tree(Float2 position)
  {
    this.branches = new ArrayList<>();
    this.position = position;
    Float2 start = position;
    int steps = 10;
    float radius = steps + 1;
    bounding = new Rectangle(new Float2(), new Float2());

    for (int i = 0; i < steps; i++)
    {
      int ax = GlobalRandom.random.nextInt(21) - 10;
      int ay = GlobalRandom.random.nextInt(21) - 10;

      Float2 end = new Float2(start.x + ax, start.y - 20 + ay);
      Capsule capsule = new Capsule(start, radius, end, radius - 1);
      start = end;
      radius--;

      branches.add(capsule);
    }
  }

  public void calculateBoundingBox()
  {
    Capsule capsule = branches.get(0);
    Float2 start = capsule.getStart();
    bounding.set(start, start);

    for (Capsule branch : branches)
    {
      bounding.growToContainX(branch.getLeft());
      bounding.growToContainX(branch.getRight());
      bounding.growToContainY(branch.getTop());
      bounding.growToContainY(branch.getBottom());
    }
  }

  public boolean contains(Float2 position)
  {
    if (bounding.contains(position))
    {
      for (Capsule branch : branches)
      {
        if (branch.contains(position))
        {
          return true;
        }
      }
    }
    return false;
  }
}

