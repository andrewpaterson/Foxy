package net.kingdom.plant.structure;

import net.kingdom.plant.Tree;

import java.awt.*;

import static net.engine.global.GlobalRandom.random;

public class Phytomer extends PlantNode
{
  private float mass;
  private float maxLength;

  public Phytomer(Tree tree, PlantNode bud, PlantNode parent, float length)
  {
    super(tree, parent, 0, bud.getAngle() + getAngleVariance(), new Color(150, 100, 150));
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
    Dispersable water = get(WATER);
    Dispersable sugar = get(SUGAR);

    if (length >= maxLength)
    {
      if (mass < 5)
      {
        if ((water.get() >= 1.0f) && (sugar.get() >= 1.0f))
        {
          if (children.size() == 1)
          {
            float rangle = randomAngle();
            children.add(new Leaf(tree, this, 10, -rangle + getAngleVariance()));
            if (random.nextInt(5) <= 2)
            {
              children.add(new AxillaryBud(tree, this, rangle + getAngleVariance()));
            }
            else
            {
              children.add(new Leaf(tree, this, 10, rangle + getAngleVariance()));
            }
            water.add(-1.0f);
            sugar.add(-1.0f);
          }
        }
      }
    }
    else
    {
      float v = maxLength * water.get();
      if (length < v)
      {
        length = v;
        if (length > maxLength)
        {
          length = maxLength;
        }
      }
    }

    if ((water.get() >= 0.01f) && (sugar.get() >= 0.01f))
    {
      mass += 0.01f;
      water.add(-0.01f);
      sugar.add(-0.01f);
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
    get(WATER).add(amount);
  }

  public float getTransferRate(int type)
  {
    return mass * super.getTransferRate(type);
  }
}

