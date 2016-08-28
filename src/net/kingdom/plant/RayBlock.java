package net.kingdom.plant;

import java.util.ArrayList;
import java.util.List;

public class RayBlock
{
  public int x;
  public int y;
  public int width;
  public int height;
  public List<RaycastObject> raycastObjects;

  public RayBlock(int x, int y, int width, int height)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.raycastObjects = new ArrayList<>();
  }
}

