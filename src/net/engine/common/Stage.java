package net.engine.common;

import net.engine.input.GameInput;

import java.awt.*;

public abstract class Stage
{
  public void stageStarting(Game game)
  {
  }

  public void stageEnding()
  {
  }

  public abstract void render(Graphics graphics, int width, int height);

  public abstract void tick(double time, GameInput input);
}

