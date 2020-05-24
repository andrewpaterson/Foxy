package net.kingdom.plant;

import java.util.ArrayList;
import java.util.List;

public class RayBlock
{
  public int left;
  public int top;
  public int width;
  public int height;
  public int right;
  public int bottom;
  public List<RaycastObject> raycastObjects;
  public int size;

  public RayBlock(int left, int top, int width, int height)
  {
    this.left = left;
    this.top = top;
    this.width = width;
    this.height = height;
    this.raycastObjects = new ArrayList<>();
    this.size = 0;
    this.right = left + width;
    this.bottom = top + height;
  }

  public void clear()
  {
    for (int i = 0; i < raycastObjects.size(); i++)
    {
      raycastObjects.set(i, null);
    }
    size = 0;
  }

  public void add(RaycastObject raycastObject)
  {
    if (raycastObjects.size() > size)
    {
      raycastObjects.set(size, raycastObject);
    }
    else
    {
      raycastObjects.add(raycastObject);
    }
    size++;
  }

  public int objectSize()
  {
    return size;
  }
}

