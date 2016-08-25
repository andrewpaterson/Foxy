package net.engine.game;

import net.engine.input.GameInput;

import java.awt.*;

public class Game
{
  protected StageManager stageManager;
  protected FrameConfig config;

  public Game(FrameConfig config)
  {
    super();
    this.config = config;
    this.stageManager = new StageManager();
  }

  protected void run()
  {
    GameFrame gameFrame = new GameFrame(config);
    gameFrame.setVisible(true);
    gameFrame.start();
    GameCanvas gameCanvas = gameFrame.getCanvas();

    long startTime = System.nanoTime();

    for (; ; )
    {
      Graphics graphics = gameCanvas.beginFrame();
      renderStage(graphics, gameCanvas.getWidth(), gameCanvas.getHeight());
      gameCanvas.render();
      gameCanvas.endFrame();


      long currentTime = System.nanoTime();
      tickStage(currentTime - startTime, gameFrame.processInput());
      startTime = currentTime;
    }
  }

  private void renderStage(Graphics graphics, int width, int height)
  {
    Stage currentStage = stageManager.getCurrentStage();
    if (currentStage != null)
    {
      currentStage.render(graphics, width, height);
    }
  }

  private void tickStage(long nanoDelta, GameInput input)
  {
    Stage currentStage = stageManager.tickStage();

    if (currentStage != null)
    {
      double time = (double) nanoDelta / (double) 1000000000;
      currentStage.tick(time, input);
    }
  }

  public void setStage(String name)
  {
    stageManager.setStage(name);
  }
}

