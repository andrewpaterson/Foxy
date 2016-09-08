package net.kingdom.plant.structure;

import java.awt.*;

public class ApicalBud extends PlantNode
{
  public ApicalBud(PlantNode parent, float angle)
  {
    super(parent, 5, angle, new Color(255, 255, 150));
  }

  @Override
  public void grow()
  {
    float water = dispersables.get(WATER).getValue();
    if (water > 1.0f)
    {
      dispersables.get(WATER).addValue(-1.0f);
      new Phytomer(this, parent, 30);
    }
  }

  @Override
  public float getMass()
  {
    return 1;
  }
}



