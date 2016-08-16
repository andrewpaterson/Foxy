package net.kingdom;

import net.engine.game.FrameConfig;
import net.engine.game.Game;

/**
 * Created by andrew on 2016/08/16.
 */
public class KingdomGame extends Game
{
  public KingdomGame()
  {
    super(new FrameConfig("Nothing Starts", 604, 340));
  }

  private void start()
  {
    stageManager.addStage("Fluid", new FluidStage());
    stageManager.setStage("Fluid");

    run();
  }

  public static void main(String[] args)
  {
    KingdomGame kingdomGame = new KingdomGame();
    kingdomGame.start();
  }
}

