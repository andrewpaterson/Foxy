package net.engine.game;

import net.engine.picture.PalettePicture;

import java.awt.*;
import java.awt.image.WritableRaster;

public class PaletteRasterWork
    extends BasePictureRasterWork
{
  private PalettePicture picture;

  public PaletteRasterWork(int width, PalettePicture picture, WritableRaster raster, int y)
  {
    super(width, raster, y);
    this.picture = picture;
  }

  @Override
  public void work()
  {
    for (int x = 0; x < width; x++)
    {
      int colourIndex = picture.unsafeGetPixel(x, y);
      Color pixelColour = picture.getPaletteColour(colourIndex);
      if (pixelColour != null)
      {
        calculationColour[3] = pixelColour.getAlpha();
        calculationColour[0] = pixelColour.getRed();
        calculationColour[1] = pixelColour.getGreen();
        calculationColour[2] = pixelColour.getBlue();
      }
      else
      {
        calculationColour[3] = 255;
        calculationColour[0] = 128;
        calculationColour[1] = 128;
        calculationColour[2] = 128;
      }
      raster.setPixel(x, y, calculationColour);
    }
  }
}

