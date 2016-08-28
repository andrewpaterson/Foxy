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
      int canvasWidth = gameCanvas.getWidth();
      int canvasHeight = gameCanvas.getHeight();

      renderStage(graphics, canvasWidth, canvasHeight);
      gameCanvas.render();
      gameCanvas.endFrame();

      long currentTime = System.nanoTime();
      tickStage(currentTime - startTime, gameFrame.getInput(), canvasWidth, canvasHeight);
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

  private void tickStage(long nanoDelta, GameInput input, int width, int height)
  {
    Stage currentStage = stageManager.tickStage();

    if (currentStage != null)
    {
      double time = (double) nanoDelta / (double) 1000000000;
      currentStage.tick(time, input, width, height);
      input.clearEvents();
    }
  }

  public void setStage(String name)
  {
    stageManager.setStage(name);
  }

  public int getWindowWidth()
  {
    return config.width;
  }

  public int getWindowHeight()
  {
    return config.height;
  }
}

