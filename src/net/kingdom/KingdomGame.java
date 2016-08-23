package net.kingdom;

import net.engine.game.FrameConfig;
import net.engine.game.Game;

public class KingdomGame extends Game
{
  public static int scale = 1;
  public static int height = 720;
  public static int width = 1280;

  public KingdomGame()
  {
    super(new FrameConfig("Nothing Starts", width * scale, height * scale));
  }

  private void start()
  {
    stageManager.addStage("Fluid", new FluidStage(5.0f, 100.0f, width, height));
    stageManager.setStage("Fluid");

    run();
  }

  public static void main(String[] args)
  {
    KingdomGame kingdomGame = new KingdomGame();
    kingdomGame.start();
  }
}

