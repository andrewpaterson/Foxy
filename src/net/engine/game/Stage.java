package net.engine.game;

import net.engine.input.GameInput;
import net.engine.thread.Threadanator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public abstract class Stage
{
  protected StageManager stageManager;

  public void stageStarting(StageManager stageManager)
  {
    this.stageManager = stageManager;
  }

  public void stageEnding()
  {
  }

  public BufferedImage convertIntsToImageRaster(int width, int height, int[] data, BufferedImage image)
  {
    Threadanator threadanator = Threadanator.getInstance();

    WritableRaster raster = image.getWritableTile(0, 0);
    for (int y = 0; y < height; y++)
    {
      int index = y * width;
      RasterParams rasterParams = new RasterParams(width, data, raster);
      threadanator.add(new RasterWork(rasterParams, y, index));
    }

    threadanator.process();

    image.releaseWritableTile(0, 0);
    return image;
  }

  public abstract void render(Graphics graphics, int width, int height);

  public abstract void tick(double time, GameInput input);
}

