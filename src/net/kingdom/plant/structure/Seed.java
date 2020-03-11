package net.kingdom.plant.structure;

import net.kingdom.plant.Tree;

import java.awt.*;

public class Seed
    extends PlantNode
{
  public Seed(Tree tree, PlantNode parent, float angle)
  {
    super(tree, parent, 0, angle, new Color(100, 100, 100));
    children.add(new ApicalBud(tree, this, 0));
    get(SUGAR).add(20);
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
}

