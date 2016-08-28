package net.kingdom;

import net.engine.game.FrameConfig;
import net.engine.game.Game;
import net.engine.thread.Threadanator;

public class KingdomGame extends Game
{
  public static int SCALE = 3;
  public static int WIDTH = 640;
  public static int HEIGHT = 360;

  public KingdomGame()
  {
    super(new FrameConfig("Simulation Window", WIDTH * SCALE, HEIGHT * SCALE));
  }

  private void start()
  {
    Threadanator.getInstance().start();

    stageManager.addStage("Fluid", new FluidStage(10.0f, 500.0f, WIDTH, HEIGHT, 20, 0.02f));
    stageManager.addStage("Plant", new PlantStage(WIDTH, HEIGHT));
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

