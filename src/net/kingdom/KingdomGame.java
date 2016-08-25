package net.kingdom;

import net.engine.game.FrameConfig;
import net.engine.game.Game;
import net.engine.thread.Threadanator;
import net.engine.thread.Work;

public class KingdomGame extends Game
{
  public static int scale = 3;
  public static int width = 604;
  public static int height = 340;

  public KingdomGame()
  {
    super(new FrameConfig("Simulation Window", width * scale, height * scale));
  }

  private void start()
  {
    Threadanator.getInstance().start();

    stageManager.addStage("Fluid", new FluidStage(5.0f, 100.0f, width, height));
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

