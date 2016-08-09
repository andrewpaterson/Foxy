package net.engine.common;

import net.engine.input.GameInput;

import java.awt.*;

public class Game
{
  protected Stage currentStage;
  protected Stage nextStage;
  protected String title;

  public Game(String title)
  {
    currentStage = null;
    nextStage = null;
    this.title = title;
  }

  protected void run()
  {
    GameFrame gameFrame = new GameFrame(title);
    gameFrame.setVisible(true);
    gameFrame.start();
    GameCanvas gameCanvas = gameFrame.getCanvas();

    long lastTick = gameCanvas.getTick();
    long startTime = System.nanoTime();

    for (; ; )
    {
      long renderTick = gameCanvas.getTick();
      if (lastTick < renderTick)
      {
        lastTick = renderTick;
        Graphics graphics = gameCanvas.beginFrame();
        renderStage(graphics, gameCanvas.getWidth(), gameCanvas.getHeight());
        gameCanvas.endFrame();


        long currentTime = System.nanoTime();
        tickStage(currentTime - startTime, gameFrame.processInput());
        startTime = currentTime;
      }
    }
  }

  private void renderStage(Graphics graphics, int width, int height)
  {
    if (currentStage != null)
    {
      currentStage.render(graphics, width, height);
    }
  }

  private void tickStage(long nanoDelta, GameInput input)
  {
    if (nextStage != null)
    {
      currentStage = nextStage;
      nextStage.stageEnding();
      nextStage = null;
      currentStage.stageStarting(this);
    }

    if (currentStage != null)
    {
      double time = (double) nanoDelta / (double) 1000000000;
      currentStage.tick(time, input);
    }
  }

  public void setStage(Stage stage)
  {
    nextStage = stage;
  }
}

