package net.kingdom.plant.structure;

import java.awt.*;

public class Leaf extends PlantNode
{
  protected float size;

  public Leaf(PlantNode parent, float length, float angle)
  {
    super(parent, length, angle, new Color(20, 255, 20));
    this.size = 0;
  }

  @Override
  public void grow()
  {
    Dispersable water = get(WATER);
    if (size < 1)
    {
      size += water.get() * 0.5f;
      if (size > 1)
      {
        size = 1.0f;
      }
      water.add(-water.get());
    }
    else
    {
      if (water.get() > 0.01f)
      {
        water.add(-0.01f);
      }
    }
    get(SUGAR).add(0.1f);
  }

  @Override
  public float getMass()
  {
    return size * 4;
  }
}

