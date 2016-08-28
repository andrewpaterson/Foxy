package net.engine.game;

import net.engine.thread.Work;

import java.awt.image.WritableRaster;

public abstract class BasePictureRasterWork extends Work
{
  protected int width;
  protected WritableRaster raster;
  protected int[] calculationColour;
  protected int y;

  public BasePictureRasterWork(int width, WritableRaster raster, int y)
  {
    this.calculationColour = new int[4];
    this.width = width;
    this.raster = raster;
    this.y = y;
  }
}
