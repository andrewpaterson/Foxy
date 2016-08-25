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
//    KingdomGame kingdomGame = new KingdomGame();
//    kingdomGame.start();

    Threadanator threadanator = Threadanator.getInstance();
    threadanator.start();

    for (int k = 0; k < 100; k++)
    {
      int finalK = k;
      Work work = new Work()
      {
        @Override
        public int work()
        {
          int x = 0;
          for (int i = 0; i < 1000; i++)
          {
            for (int j = 0; j < 1000; j++)
            {
              x += i - j;
            }
          }
          System.out.println((x + finalK));
          return SUCCESS;
        }
      };
      threadanator.add(work);
    }
    threadanator.addWait();

    threadanator.process();

    for (int k = 0; k < 100; k++)
    {
      int finalK = k;
      Work work = new Work()
      {
        @Override
        public int work()
        {
          int x = 0;
          for (int i = 0; i < 1000; i++)
          {
            for (int j = 0; j < 1000; j++)
            {
              x += i - j;
            }
          }
          System.out.println((x + finalK));
          return SUCCESS;
        }
      };
      threadanator.add(work);
    }
    threadanator.addStop();

    threadanator.process();
  }
}

