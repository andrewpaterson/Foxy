package net.foxy;

import net.engine.cel.CelStore;
import net.engine.game.FrameConfig;
import net.engine.game.Game;
import net.foxy.loading.LoadingScreen;
import net.foxy.nights.NightOne;
import net.foxy.title.TitleScreen;

public class FoxyGame
    extends Game
{
  private CelStore celStore;

  public FoxyGame()
  {
    super(new FrameConfig("Five Nights at Foxy's", 1280, 720));
    celStore = new CelStore();
  }

  private void start()
  {
    stageManager.addStage("Loading", new LoadingScreen(celStore));
    stageManager.addStage("Title", new TitleScreen(celStore));
    stageManager.addStage("Night One", new NightOne(celStore));

    setStage("Loading");
    run();
  }

  public static void main(String[] args)
  {
    FoxyGame game = new FoxyGame();
    game.start();
  }
}

