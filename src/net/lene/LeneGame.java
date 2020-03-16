package net.lene;

import net.engine.game.FrameConfig;
import net.engine.game.Game;
import net.engine.thread.Threadanator;

/**
 * Copyright (c) Lautus Solutions.
 */
public class LeneGame
    extends Game
{
  public LeneGame()
  {
    super(new FrameConfig("Window", 1024, 768));
  }

  private void start()
  {
    Threadanator.getInstance().start();

    stageManager.addStage("First Try", new LeneStage(1024, 768));
    stageManager.setStage("First Try");

    run();

    Threadanator.getInstance().stop();
  }

  public static void main(String[] args)
  {
    LeneGame leneGame = new LeneGame();
    leneGame.start();
  }
}

