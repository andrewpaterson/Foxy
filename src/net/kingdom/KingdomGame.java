package net.kingdom;

import net.engine.game.FrameConfig;
import net.engine.game.Game;
import net.engine.thread.Threadanator;

public class KingdomGame extends Game
{
  public static int SCALE = 3;
  public static int RENDER_WIDTH = 640;
  public static int RENDER_HEIGHT = 360;

  public KingdomGame()
  {
    super(new FrameConfig("Simulation Window", RENDER_WIDTH * SCALE, RENDER_HEIGHT * SCALE));
  }

  private void start()
  {
    Threadanator.getInstance().start();

    stageManager.addStage("Fluid", new FluidStage(10.0f, 500.0f, 20, 0.02f, RENDER_WIDTH, RENDER_HEIGHT, getWindowWidth(), getWindowHeight()));
    stageManager.addStage("Plant", new PlantStage(RENDER_WIDTH, RENDER_HEIGHT, getWindowWidth(), getWindowHeight()));
    stageManager.setStage("Plant");

    run();

    Threadanator.getInstance().stop();
  }

  public static void main(String[] args)
  {
    KingdomGame kingdomGame = new KingdomGame();
    kingdomGame.start();
  }
}

