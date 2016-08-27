package net.engine.game;

import net.engine.thread.Work;

import java.awt.image.WritableRaster;

public class RasterWork extends Work
{
  private int width;
  private int[] data;
  private WritableRaster raster;
  private int[] colour;
  private int y;
  private int index;

  public RasterWork(int width, int[] data, WritableRaster raster, int y, int index)
  {
    this.y = y;
    this.index = index;
    this.colour = new int[4];
    this.width = width;
    this.data = data;
    this.raster = raster;
  }

  @Override
  public void work()
  {
    for (int x = 0; x < width; x++)
    {
      int color = data[index + x];
      colour[3] = (color & 0xff000000) >>> 24;
      colour[0] = color & 0xff;
      colour[1] = (color & 0xff00) >>> 8;
      colour[2] = (color & 0xff0000) >>> 16;
      raster.setPixel(x, y, colour);
    }
  }
}

