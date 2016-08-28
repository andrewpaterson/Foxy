package net.engine.game;

import net.engine.picture.Picture;
import net.engine.thread.Work;

import java.awt.*;
import java.awt.image.WritableRaster;

public class PaletteRasterWork extends Work
{
  private int width;
  private WritableRaster raster;
  private int[] calculationColour;
  private Picture picture;
  private int y;

  public PaletteRasterWork(int width, Picture picture, WritableRaster raster, int y)
  {
    this.picture = picture;
    this.y = y;
    this.calculationColour = new int[4];
    this.width = width;
    this.raster = raster;
  }

  @Override
  public void work()
  {
    for (int x = 0; x < width; x++)
    {
      int colourIndex = picture.getPixel(x, y);
      Color pixelColour = picture.getPaletteColour(colourIndex);
      this.calculationColour[3] = pixelColour.getAlpha();
      this.calculationColour[0] = pixelColour.getRed();
      this.calculationColour[1] = pixelColour.getGreen();
      this.calculationColour[2] = pixelColour.getBlue();
      raster.setPixel(x, y, this.calculationColour);
    }
  }
}

