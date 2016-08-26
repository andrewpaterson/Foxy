package net.engine.game;

import net.engine.thread.Work;

public class RasterWork extends Work
{
  private RasterParams params;
  private int[] colour;
  private int y;
  private int index;

  public RasterWork(RasterParams params, int y, int index)
  {
    this.y = y;
    this.index = index;
    this.colour = new int[4];
    this.params = params;
  }

  @Override
  public void work()
  {
    for (int x = 0; x < params.width; x++)
    {
      int color = params.data[index + x];
      colour[3] = (color & 0xff000000) >>> 24;
      colour[0] = color & 0xff;
      colour[1] = (color & 0xff00) >>> 8;
      colour[2] = (color & 0xff0000) >>> 16;
      params.raster.setPixel(x, y, colour);
    }
  }
}

