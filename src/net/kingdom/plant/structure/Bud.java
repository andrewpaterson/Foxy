package net.kingdom.plant.structure;

import net.engine.global.GlobalRandom;
import net.kingdom.plant.Tree;

import java.awt.*;

public abstract class Bud extends PlantNode
{
  public Bud(Tree tree, PlantNode parent, float angle)
  {
    super(tree, parent, 5, angle, new Color(255, 255, 20));
  }

  @Override
  public void grow()
  {
    Dispersable water = get(WATER);
    Dispersable sugar = get(SUGAR);

    if ((water.get() >= 1.0f) && (sugar.get() >= 1.0f))
    {
      water.add(-1.0f);
      sugar.add(-1.0f);
      new Phytomer(tree,  this, parent, 20 + GlobalRandom.random.nextInt(20));
    }
  }

  @Override
  public float getMass()
  {
    return 1;
  }
}

