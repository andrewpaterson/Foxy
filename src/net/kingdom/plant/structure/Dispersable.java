package net.kingdom.plant.structure;

import java.util.List;

public class Dispersable
{
  public float value;
  public float delta;
  public int type;

  public Dispersable(int type)
  {
    this.value = 0;
    this.delta = 0;
    this.type = type;
  }

  public void preDisperse()
  {
    this.delta = 0;
  }

  public void postDisperse()
  {
    this.value += delta;
  }

  public void disperse(PlantNode plantNode)
  {
    List<PlantNode> adjacentNodes = plantNode.getAdjacentNodes();

    int lowPressures = 0;
    for (PlantNode adjacentNode : adjacentNodes)
    {
      if (adjacentNode.getValue(type) < value)
      {
        lowPressures++;
      }
    }
    lowPressures += 1;

    for (PlantNode adjacentNode : adjacentNodes)
    {
      float difference = value - adjacentNode.getValue(type);
      if (difference > 0)
      {
        float dispersed = (difference / lowPressures) * plantNode.getTransferRate(type);
        adjacentNode.delta(type, dispersed);
        delta -= dispersed;
      }
    }
  }

  public void add(float amount)
  {
    this.value += amount;
  }

  public void delta(float amount)
  {
    this.delta += amount;
  }

  public float get()
  {
    return value;
  }
}

