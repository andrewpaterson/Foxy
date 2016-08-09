package net.engine.common;

import net.engine.input.GameInput;

import java.awt.*;

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

  public abstract void render(Graphics graphics, int width, int height);

  public abstract void tick(double time, GameInput input);
}

