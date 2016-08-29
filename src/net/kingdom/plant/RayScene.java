package net.kingdom.plant;

import java.util.ArrayList;
import java.util.List;

public class RayScene
{
  protected int blockWidth;
  protected int blockHeight;
  protected int width;
  protected int height;
  protected RayBlock[] blocks;
  protected int blockRows;
  protected int blockColumns;
  protected List<RaycastObject> raycastObjects;

  public RayScene(int sceneWidth, int sceneHeight, int blockWidth, int blockHeight)
  {
    this.width = sceneWidth;
    this.height = sceneHeight;
    this.blockWidth = blockWidth;
    this.blockHeight = blockHeight;

    blockColumns = ceil(sceneWidth, blockWidth);
    blockRows = ceil(sceneHeight, blockHeight);

    blocks = createBlocks(sceneWidth, sceneHeight, blockWidth, blockHeight);

    raycastObjects = new ArrayList<>();
  }

  public int IX(int x, int y)
  {
    return x + blockColumns * y;
  }

  private RayBlock[] createBlocks(int sceneWidth, int sceneHeight, int blockWidth, int blockHeight)
  {
    RayBlock[] blocks = new RayBlock[blockRows * blockColumns];
    for (int y = 0; y < blockRows; y++)
    {
      for (int x = 0; x < blockColumns; x++)
      {
        int extentWidth = calculateExtent(sceneWidth, blockWidth, x);
        int extentHeight = calculateExtent(sceneHeight, blockHeight, y);
        blocks[x + y * blockColumns] = new RayBlock(x * blockWidth, y * blockHeight, extentWidth, extentHeight);
      }
    }
    return blocks;
  }

  private int calculateExtent(int sceneSize, int blockSize, int index)
  {
    int extentWidth = index * blockSize + blockSize;
    if (extentWidth > sceneSize)
    {
      return blockSize - (extentWidth - sceneSize);
    }
    else
    {
      return blockSize;
    }
  }

  private int ceil(int dividend, int divisor)
  {
    int quotient = dividend / divisor;
    if (dividend % divisor != 0)
    {
      quotient++;
    }
    return quotient;
  }

  private int ceil(float dividend, int divisor)
  {
    int intQuotient = (int) (dividend / divisor);
    float floatQuotient = dividend / divisor;

    if (floatQuotient > (float) intQuotient)
    {
      intQuotient++;
    }
    return intQuotient;
  }


  public void add(RaycastObject object)
  {
    raycastObjects.add(object);
  }

  public void calculateOverlaps()
  {
    for (RaycastObject raycastObject : raycastObjects)
    {
      raycastObject.calculateBoundingBox();
    }

    clearBlocks();

    List<RaycastObject> raycastObjects = this.raycastObjects;
    addRaycastObjectsToBlocks(raycastObjects);
  }

  private void addRaycastObjectsToBlocks(List<RaycastObject> raycastObjects)
  {
    for (RaycastObject raycastObject : raycastObjects)
    {
      if (raycastObject.isGroup())
      {
        RaycastGroup group = (RaycastGroup) raycastObject;
        List<RaycastObject> groupObjects = (List<RaycastObject>) group.getObjects();
        addRaycastObjectsToBlocks(groupObjects);
      }
      else
      {
        addRaycastObjectToBlocks(raycastObject);
      }
    }
  }

  private void clearBlocks()
  {
    for (RayBlock block : blocks)
    {
      block.clear();
    }
  }

  private void addRaycastObjectToBlocks(RaycastObject raycastObject)
  {
    int left = (int) (raycastObject.getLeft() / blockWidth);
    int right = ceil(raycastObject.getRight(), blockWidth);

    int top = (int) (raycastObject.getTop() / blockHeight);
    int bottom = ceil(raycastObject.getBottom(), blockHeight);

    if ((bottom < 0) || (left > blockColumns - 1) || (top > blockRows - 1) || (right < 0))
    {
      return;
    }

    if (top < 0)
    {
      top = 0;
    }
    if (bottom > blockRows - 1)
    {
      bottom = blockRows - 1;
    }
    if (left < 0)
    {
      left = 0;
    }
    if (right > blockColumns - 1)
    {
      right = blockColumns - 1;
    }


    for (int y = top; y <= bottom; y++)
    {
      for (int x = left; x <= right; x++)
      {
        RayBlock block = blocks[IX(x, y)];
        block.add(raycastObject);
      }
    }
  }

  public RayBlock[] getBlocks()
  {
    return blocks;
  }
}

