package net.kingdom.plant;

import net.engine.math.Float2;
import net.engine.math.Spline;
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
    Spline spline = new Spline(
            new Float2(position),
            new Float2(position).add(-50, -50),
            new Float2(position).add(50, -100),
            new Float2(position).add(0, -150));
    int steps = 20;
    float[] xs = new float[steps];
    float[] ys = new float[steps];
    spline.generate(steps, xs, ys);
    for (int i = 0; i < steps - 1; i++)
    {
      Capsule capsule = new Capsule(new Float2(xs[i], ys[i]), 5, new Float2(xs[i + 1], ys[i + 1]), 5);
      branches.add(new Branch(capsule));
    }
//    Float2 start = position;
//    int steps = 10;
//    float radius = steps*2 + 2;
//
//    for (int i = 0; i < steps; i++)
//    {
//      int ax = GlobalRandom.random.nextInt(21) - 10;
//      int ay = GlobalRandom.random.nextInt(21) - 10;
//
//      Float2 end = new Float2(start.x + ax, start.y - 20 + ay);
//      Capsule capsule = new Capsule(start, radius, end, radius - 1);
//      start = end;
//      radius-=2;
//
//      branches.add(new Branch(capsule));
//    }
  }

  @Override
  public List<? extends RaycastObject> getObjects()
  {
    return branches;
  }
}

