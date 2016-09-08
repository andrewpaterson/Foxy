package net.kingdom.plant.structure;

import net.engine.global.GlobalRandom;

import java.awt.*;

public class AxillaryBud extends PlantNode
{
  public AxillaryBud(PlantNode parent, float angle)
  {
    super(parent, 5, angle, new Color(255, 255, 20));
  }

  @Override
  public void grow()
  {
    float water = dispersables.get(WATER).getValue();
    if (water > 1.0f)
    {
      dispersables.get(WATER).addValue(-1.0f);
      new Phytomer(this, parent, 20 + GlobalRandom.random.nextInt(20));
    }
  }

  @Override
  public float getMass()
  {
    return 1;
  }
}

