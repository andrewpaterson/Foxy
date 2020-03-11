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
      this.calculationColour[3] = pixelColour.getAlpha();
      this.calculationColour[0] = pixelColour.getRed();
      this.calculationColour[1] = pixelColour.getGreen();
      this.calculationColour[2] = pixelColour.getBlue();
      raster.setPixel(x, y, this.calculationColour);
    }
  }
}

