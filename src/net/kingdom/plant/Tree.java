package net.kingdom.plant;

import net.engine.math.Float2;
import net.engine.shape.Capsule;
import net.kingdom.plant.structure.PlantNode;
import net.kingdom.plant.structure.Seed;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Tree extends RaycastGroup
{
  private List<RaycastCapsule> capsules;
  private PlantNode start;
  private Float2 position;
  private TreeDebug debug;

  public Tree(Float2 position, TreeDebug debug)
  {
    super();
    this.position = position;
    this.debug = debug;
    capsules = new ArrayList<>();

    start = new Seed(null, 0);
  }

  @Override
  public List<? extends RaycastObject> getObjects()
  {
    return capsules;
  }

  public List<PlantNode> collectNodes()
  {
    ArrayList<PlantNode> nodes = new ArrayList<>();
    start.collectNodes(nodes);
    return nodes;
  }

  public void grow()
  {
    List<PlantNode> nodes = collectNodes();
    for (PlantNode node : nodes)
    {
      node.preDisperse();
    }
    for (PlantNode node : nodes)
    {
      node.disperse();
    }
    for (PlantNode node : nodes)
    {
      node.postDisperse();
    }
    for (PlantNode node : nodes)
    {
      node.grow();
    }

    capsules = new ArrayList<>();

    addCapsule(this.position, 0, start);
  }

  private void addCapsule(Float2 startPos, float startAngle, PlantNode plantNode)
  {
    float length = plantNode.getLength();
    float localAngle = plantNode.getAngle();
    float angle = startAngle + localAngle;
    Float2 end = rotate(length, angle + 180);
    end.add(startPos);

    if (length > 0.00001f)
    {
      capsules.add(new RaycastCapsule(new Capsule(startPos,
              (float) Math.sqrt(plantNode.getMass()),
              end,
              (float) Math.sqrt(plantNode.getMass())),
              getDebugColour(plantNode)));
    }

    List<PlantNode> childNodes = plantNode.getChildNodes();
    for (PlantNode childNode : childNodes)
    {
      addCapsule(end, angle, childNode);
    }
  }

  private Color getDebugColour(PlantNode plantNode)
  {
    return debug.getDebugColour(plantNode);
  }

  private Float2 rotate(float length, float angle)
  {
    return new Float2(length * sin(angle), length * cos(angle));
  }

  private float cos(float angle)
  {
    return (float) Math.cos(angle * (Math.PI / 180));
  }

  private float sin(float angle)
  {
    return (float) Math.sin(angle * (Math.PI / 180));
  }

  public void water(float amount)
  {
    start.addWater(amount);
  }
}

