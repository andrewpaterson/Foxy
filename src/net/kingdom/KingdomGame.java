package net.kingdom;

import net.engine.game.FrameConfig;
import net.engine.game.Game;

/**
 * Created by andrew on 2016/08/16.
 */
public class KingdomGame extends Game
{
  public static int scale = 3;
  public static int height = 340;
  public static int width = 604;

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

