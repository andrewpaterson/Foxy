package net.kingdom.plant.structure;

import net.engine.global.GlobalRandom;

import java.awt.*;

public abstract class Bud extends PlantNode
{
  public Bud(PlantNode parent, float angle)
  {
    super(parent, 5, angle, new Color(255, 255, 20));
  }

  @Override
  public void grow()
  {
    Dispersable water = get(WATER);
    Dispersable sugar = get(SUGAR);

    if ((water.get() > 1.0f) && (sugar.get() > 1.0f))
    {
      water.add(-1.0f);
      new Phytomer(this, parent, 20 + GlobalRandom.random.nextInt(20));
    }
  }

  @Override
  public float getMass()
  {
    return 1;
  }
}

