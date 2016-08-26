package net.engine.game;

import java.awt.image.WritableRaster;

public class RasterParams
{
  public int width;
  public int[] data;
  public WritableRaster raster;

  public RasterParams(int width, int[] data, WritableRaster raster)
  {
    this.width = width;
    this.data = data;
    this.raster = raster;
  }
}

