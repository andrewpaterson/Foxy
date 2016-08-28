package net.kingdom.plant;

import net.engine.global.GlobalRandom;
import net.engine.math.Float2;
import net.engine.shape.Capsule;

import java.util.ArrayList;
import java.util.List;

public class Tree
{
  public Float2 position;
  public List<Capsule> branches;

  public Tree(Float2 position)
  {
    this.branches = new ArrayList<>();
    this.position = position;
    Float2 start = position;
    float radius = 5;

    for (int i = 0; i < 5; i++)
    {
      int ax = GlobalRandom.random.nextInt(11) - 5;
      int ay = GlobalRandom.random.nextInt(11) - 5;

      Float2 end = new Float2(start.x + ax, start.y - 20 + ay);
      Capsule capsule = new Capsule(start, radius, end, radius - 1);
      start = end;
      radius--;

      branches.add(capsule);
    }
  }
}

