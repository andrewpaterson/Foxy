package net.kingdom;

import net.engine.game.FrameConfig;
import net.engine.game.Game;
import net.engine.thread.Threadanator;

public class KingdomGame extends Game
{
  public static int scale = 1;
  public static int width = 1280;
  public static int height = 720;

  public KingdomGame()
  {
    super(new FrameConfig("Simulation Window", width * scale, height * scale));
  }

  private void start()
  {
    Threadanator.getInstance().start();

    stageManager.addStage("Fluid", new FluidStage(2.0f, 100.0f, width, height, 5, 0.02f));
    stageManager.setStage("Fluid");

    run();

    Threadanator.getInstance().stop();
  }

  public static void main(String[] args)
  {
    KingdomGame kingdomGame = new KingdomGame();
    kingdomGame.start();
  }
}

