package net.foxy;

import net.engine.common.Game;
import net.foxy.loading.LoadingScreen;

public class FoxyGame extends Game
{
  public FoxyGame()
  {
    super("Five Nights at Foxy's");
  }

  public static void main(String[] args)
  {
    FoxyGame game = new FoxyGame();

    game.setStage(new LoadingScreen());
    game.run();
  }
}

