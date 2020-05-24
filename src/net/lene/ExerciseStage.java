package net.lene;

import net.engine.game.PictureStage;
import net.engine.input.GameInput;
import net.engine.picture.PalettePicture;
import net.engine.picture.Picture;
import net.engine.picture.palette.CommodorePalette;

import java.awt.*;
import java.util.Random;

/**
 * Copyright (c) Lautus Solutions.
 */
public abstract class ExerciseStage extends PictureStage
{
  public ExerciseStage(int renderWidth, int renderHeight)
  {
    super(new PalettePicture(renderWidth, renderHeight, CommodorePalette.create()));
  }

  @Override
  public void render(Graphics graphics, int width, int height)
  {
    renderPictureToWindow(graphics, width, height);
  }

  @Override
  public void tick(double time, GameInput input, int width, int height)
  {

  }

  protected void sleep(int millis)
  {
    try
    {
      Thread.sleep(millis);
    }
    catch (InterruptedException ignored)
    {
    }
  }
}

