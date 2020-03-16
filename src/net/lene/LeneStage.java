package net.lene;

import net.engine.game.PictureStage;
import net.engine.input.GameInput;
import net.engine.picture.ComponentPicture;

import java.awt.*;

/**
 * Copyright (c) Lautus Solutions.
 */
public class LeneStage
    extends PictureStage
{
  public LeneStage(int renderWidth, int renderHeight)
  {
    super(new ComponentPicture(renderWidth, renderHeight));
  }

  @Override
  public void render(Graphics graphics, int width, int height)
  {
  }

  @Override
  public void tick(double time, GameInput input, int width, int height)
  {
  }
}

