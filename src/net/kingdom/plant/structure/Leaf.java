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
    if (size < 1)
    {
      float water = dispersables.get(WATER).getValue();
      size += water * 0.5f;
      if (size > 1)
      {
        size = 1.0f;
      }
      dispersables.get(WATER).addValue(-water);
    }
  }

  @Override
  public float getMass()
  {
    return size * 4;
  }
}

