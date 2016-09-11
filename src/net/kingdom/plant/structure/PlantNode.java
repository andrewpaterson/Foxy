package net.kingdom.plant.structure;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class PlantNode
{
  public static int WATER = 0;
  public static int SUGAR = 1;

  protected Color debugColour;

  protected List<PlantNode> children;
  protected PlantNode parent;

  protected float length;
  protected float angle;

  protected List<Dispersable> dispersables;

  public PlantNode(PlantNode parent, float length, float angle, Color debugColour)
  {
    this.debugColour = debugColour;

    this.parent = parent;
    this.children = new ArrayList<>();

    this.length = length;
    this.angle = angle;

    this.dispersables = new ArrayList<>();
    this.dispersables.add(new Dispersable(WATER));
    this.dispersables.add(new Dispersable(SUGAR));
  }

  public void collectNodes(List<PlantNode> nodes)
  {
    nodes.add(this);
    List<PlantNode> childNodes = getChildNodes();
    if (childNodes != null)
    {
      for (PlantNode childNode : childNodes)
      {
        childNode.collectNodes(nodes);
      }
    }
  }

  protected List<PlantNode> getAdjacentNodes()
  {
    List<PlantNode> childNodes = getChildNodes();
    List<PlantNode> adjacentNodes = new ArrayList<>();
    if (parent != null)
    {
      adjacentNodes.add(parent);
    }
    if (childNodes != null)
    {
      adjacentNodes.addAll(childNodes);
    }
    return adjacentNodes;
  }

  public PlantNode replace(PlantNode replacee, PlantNode replacement)
  {
    for (int i = 0; i < children.size(); i++)
    {
      PlantNode current = children.get(i);
      if (current == replacee)
      {
        children.set(i, replacement);
        return current;
      }
    }
    return replacee;
  }

  public List<PlantNode> getChildNodes()
  {
    return children;
  }

  public void addWater(float amount)
  {
    dispersables.get(WATER).add(amount);
  }

  public float getAngle()
  {
    return angle;
  }

  public void setAngle(float angle)
  {
    this.angle = angle;
  }

  public float getLength()
  {
    return length;
  }

  public abstract void grow();

  public Color getDebugColour()
  {
    return debugColour;
  }

  protected Dispersable get(int type)
  {
    return dispersables.get(type);
  }

  public abstract float getMass();

  public void preDisperse()
  {
    for (Dispersable dispersable : dispersables)
    {
      dispersable.preDisperse();
    }
  }

  public void disperse()
  {
    for (Dispersable dispersable : dispersables)
    {
      dispersable.disperse(this);
    }
  }

  public void postDisperse()
  {
    for (Dispersable dispersable : dispersables)
    {
      dispersable.postDisperse();
    }
  }

  public float getValue(int type)
  {
    return dispersables.get(type).value;
  }

  public void delta(int type, float amount)
  {
    dispersables.get(type).delta(amount);
  }

  public float getTransferRate()
  {
    return 0.1f;
  }
}

