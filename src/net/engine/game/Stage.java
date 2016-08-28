package net.engine.game;

import net.engine.common.EngineException;
import net.engine.input.*;
import net.engine.picture.Picture;
import net.engine.thread.Threadanator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public abstract class Stage implements InputHandler
{
  protected StageManager stageManager;

  public void stageStarting(StageManager stageManager)
  {
    this.stageManager = stageManager;
  }

  public void stageEnding()
  {
  }

  //Does not belong here.
  public BufferedImage convertComponentPictureToImageRaster(int width, int height, int[] data, BufferedImage image)
  {
    Threadanator threadanator = Threadanator.getInstance();

    WritableRaster raster = image.getWritableTile(0, 0);
    for (int y = 0; y < height; y++)
    {
      int index = y * width;
      threadanator.add(new ComponentRasterWork(width, data, raster, y, index));
    }

    threadanator.process(16);

    image.releaseWritableTile(0, 0);
    return image;
  }

  //Does not belong here.
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
      threadanator.add(new PaletteRasterWork(pictureWidth, picture, raster, y));
    }

    threadanator.process(16);

    image.releaseWritableTile(0, 0);
    return image;
  }

  @Override
  public void mouseInput(MouseInput input)
  {
  }

  @Override
  public void keyInput(KeyInput input)
  {
  }

  @Override
  public void pointerInput(PointerInput input)
  {
  }

  @Override
  public void wheelInput(WheelInput input)
  {
  }

  public abstract void render(Graphics graphics, int width, int height);

  public abstract void tick(double time, GameInput input);
}

