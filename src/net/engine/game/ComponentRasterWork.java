package net.engine.game;

import net.engine.picture.Colour;
import net.engine.picture.ComponentPicture;

import java.awt.image.WritableRaster;

public class ComponentRasterWork
    extends BasePictureRasterWork
{
  private ComponentPicture picture;

  public ComponentRasterWork(int width, ComponentPicture picture, WritableRaster raster, int y)
  {
    super(width, raster, y);
    this.picture = picture;
  }

  @Override
  public void work()
  {
    for (int x = 0; x < width; x++)
    {
      int pixelColour = picture.unsafeGetPixel(x, y);
      this.calculationColour[3] = Colour.getAlpha(pixelColour);
      this.calculationColour[0] = Colour.getRed(pixelColour);
      this.calculationColour[1] = Colour.getGreen(pixelColour);
      this.calculationColour[2] = Colour.getBlue(pixelColour);
      raster.setPixel(x, y, this.calculationColour);
    }
  }
}

