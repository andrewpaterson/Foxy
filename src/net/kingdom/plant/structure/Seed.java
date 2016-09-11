package net.kingdom.plant.structure;

import java.awt.*;

public class Seed extends PlantNode
{
  public Seed(PlantNode parent, float angle)
  {
    super(parent, 0, angle, new Color(100, 100, 100));
    children.add(new ApicalBud(this, 0));
    get(SUGAR).add(10);
  }

  @Override
  public void grow()
  {
  }

  @Override
  public float getMass()
  {
    return 3;
  }

  protected boolean isSeed()
  {
    return true;
  }
}

