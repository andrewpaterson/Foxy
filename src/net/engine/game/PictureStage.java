package net.engine.game;

import net.engine.common.EngineException;
import net.engine.common.Timer;
import net.engine.picture.Picture;
import net.engine.picture.ComponentPicture;
import net.engine.picture.PalettePicture;
import net.engine.thread.Threadanator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public abstract class PictureStage extends Stage
{
  protected Picture picture;
  protected BufferedImage bufferedImage;
  protected Timer timer;

  public PictureStage(Picture picture)
  {
    this.picture = picture;
    this.bufferedImage = new BufferedImage(picture.getWidth(), picture.getHeight(), BufferedImage.TYPE_INT_ARGB);
    this.timer = new Timer();
  }

  protected void renderPictureToWindow(Graphics graphics, int windowWidth, int windowHeight)
  {
    convertPalettePictureToImageRaster(picture, bufferedImage);
    graphics.drawImage(bufferedImage, 0, 0, windowWidth, windowHeight, 0, 0, picture.getWidth(), picture.getHeight(), null);
  }

  public BufferedImage convertPalettePictureToImageRaster(Picture picture, BufferedImage image)
  {
    Threadanator threadanator = Threadanator.getInstance();

    WritableRaster raster = image.getWritableTile(0, 0);
    int pictureHeight = picture.getHeight();
    int pictureWidth = picture.getWidth();
    if ((raster.getWidth() != pictureWidth) || (raster.getHeight() != pictureHeight))
    {
      throw new EngineException("Raster size [%s, %s] does not match Picture size [%s, %s].", raster.getWidth(), raster.getHeight(), pictureWidth, pictureHeight);
    }

    for (int y = 0; y < pictureHeight; y++)
    {
      if (picture instanceof PalettePicture)
      {
        threadanator.add(new PaletteRasterWork(pictureWidth, (PalettePicture) picture, raster, y));
      }
      else
      {
        threadanator.add(new ComponentRasterWork(pictureWidth, (ComponentPicture) picture, raster, y));
      }
    }

    threadanator.process(16);

    image.releaseWritableTile(0, 0);
    return image;
  }

  protected float widthScale(float windowWidth)
  {
    return picture.getWidth() / windowWidth;
  }

  protected float heightScale(float windowHeight)
  {
    return picture.getHeight() / windowHeight;
  }
}

