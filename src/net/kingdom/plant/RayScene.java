package net.kingdom.plant;

import java.util.ArrayList;
import java.util.List;

public class RayScene
{
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

    blockColumns = calculateBlockColumns(sceneWidth, blockWidth);
    blockRows = calculateBlockRows(sceneHeight, blockHeight);

    blocks = createBlocks(sceneWidth, sceneHeight, blockWidth, blockHeight);

    raycastObjects = new ArrayList<>();
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
        blocks[x + y * blockColumns] = new RayBlock(x, y, extentWidth, extentHeight);
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

  private int calculateBlockColumns(int sceneWidth, int blockWidth)
  {
    int blockColumns = sceneWidth / blockWidth;
    if (sceneWidth % blockWidth != 0)
    {
      blockColumns++;
    }
    return blockColumns;
  }

  private int calculateBlockRows(int sceneHeight, int blockHeight)
  {
    int blockRows = sceneHeight / blockHeight;
    if (sceneHeight % blockHeight != 0)
    {
      blockRows++;
    }
    return blockRows;
  }

  public void add(RaycastObject object)
  {
    raycastObjects.add(object);
  }

  public void calculateOverlaps()
  {
    for (RaycastObject raycastObject : raycastObjects)
    {
      xxx // Do division to work this out.
    }
  }
}

