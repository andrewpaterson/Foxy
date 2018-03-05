package net.kingdom;

import net.engine.game.FrameConfig;
import net.engine.game.Game;
import net.engine.thread.Threadanator;

public class KingdomGame extends Game
{
  public static int SCALE = 2;
  public static int RENDER_WIDTH = (int) (2560/SCALE)-80;
  public static int RENDER_HEIGHT = (int) (1440/SCALE)-80;

  public KingdomGame()
  {
    super(new FrameConfig("Simulation Window", RENDER_WIDTH * SCALE, RENDER_HEIGHT * SCALE));
  }

  private void start()
  {
    Threadanator.getInstance().start();

    stageManager.addStage("Fluid", new FluidStage(10.0f, 500.0f, 10, 0.02f, RENDER_WIDTH, RENDER_HEIGHT));
    stageManager.addStage("Plant", new PlantStage(RENDER_WIDTH, RENDER_HEIGHT));
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

