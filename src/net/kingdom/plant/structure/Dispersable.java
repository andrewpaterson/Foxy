package net.kingdom.plant.structure;

import java.util.List;

public class Dispersable
{
  public float value;
  public float calcValue;
  public int type;

  public Dispersable(int type)
  {
    this.value = 0;
    this.calcValue = 0;
    this.type = type;
  }

  public void preDisperse()
  {
    this.calcValue = 0;
  }

  public void postDisperse()
  {
    this.value += calcValue;
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
        float dispersed = (difference / lowPressures) * 0.5f;
        adjacentNode.addCalcValue(type, dispersed);
        calcValue -= dispersed;
      }
    }
  }

  public void addValue(float amount)
  {
    this.value += amount;
  }

  public void addCalcValue(float amount)
  {
    this.calcValue += amount;
  }

  public float getValue()
  {
    return value;
  }
}

