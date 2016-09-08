package net.kingdom.plant.structure;

import java.awt.*;

import static net.engine.global.GlobalRandom.random;

public class Phytomer extends PlantNode
{
  private float mass;
  private float maxLength;

  public Phytomer(PlantNode bud, PlantNode parent, float length)
  {
    super(parent, 0, bud.getAngle() + getAngleVariance(), new Color(150, 100, 150));
    mass = 1;
    maxLength = length;
    children.add(bud);
    PlantNode budParent = bud.parent;
    bud.parent = this;
    bud.setAngle(0);
    if (budParent != null)
    {
      budParent.replace(bud, this);
    }
  }

  @Override
  public void grow()
  {
    float water = dispersables.get(WATER).getValue();
    if (length >= maxLength)
    {
      if (water > 1.0f)
      {
        if (children.size() == 1)
        {
          float rangle = randomAngle();
          children.add(new Leaf(this, 10, -rangle + getAngleVariance()));
          if (random.nextInt(5) <= 2)
          {
            children.add(new AxillaryBud(this, rangle + getAngleVariance()));
          }
          else
          {
            children.add(new Leaf(this, 10, rangle + getAngleVariance()));
          }
          dispersables.get(WATER).addValue(-1.0f);
          children.get(0).dispersables.get(WATER).addValue(1);
        }
        else
        {
          if (mass < 5)
          {
            mass += 0.01;
            dispersables.get(WATER).addValue(-0.01f);
          }
        }
      }
    }
    else
    {
      float v = maxLength * water;
      if (length < v)
      {
        length = v;
        if (length > maxLength)
        {
          length = maxLength;
        }
      }
    }
  }

  @Override
  public float getMass()
  {
    return mass;
  }

  private float randomAngle()
  {
    int nextInt = random.nextInt(2);
    float angle;
    if (nextInt == 0)
    {
      angle = 45;
    }
    else
    {
      angle = -45;
    }
    return angle;
  }

  public static int getAngleVariance()
  {
    return random.nextInt(21) - 10;
  }

  public void addWater(float amount)
  {
    dispersables.get(WATER).addValue(amount);
  }
}

