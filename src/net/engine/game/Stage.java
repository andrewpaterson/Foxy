package net.engine.game;

import net.engine.input.*;

import java.awt.*;

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

  @Override
  public void mouseInput(MouseInput input, GameInput gameInput, int width, int height)
  {
  }

  @Override
  public void keyInput(KeyInput input, GameInput gameInput, int width, int height)
  {
  }

  @Override
  public void pointerInput(PointerInput input, GameInput gameInput, int width, int height)
  {
  }

  @Override
  public void wheelInput(WheelInput input, GameInput gameInput, int width, int height)
  {
  }

  public abstract void render(Graphics graphics, int width, int height);

  public abstract void tick(double time, GameInput input, int width, int height);
}

