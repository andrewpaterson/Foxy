package net.foxy.loading;

import net.engine.cel.CelStore;
import net.engine.game.StageManager;
import net.engine.initial.Loader;
import net.engine.input.GameInput;
import net.foxy.FoxyStage;

import java.awt.*;

import static net.engine.global.GlobalRandom.random;

public class LoadingScreen extends FoxyStage
{
  private Loader loader;

  public LoadingScreen(CelStore celStore)
  {
    super(celStore);
    this.loader = new FoxyLoader(celStore);
  }

  @Override
  public void stageStarting(StageManager stageManager)
  {
    super.stageStarting(stageManager);
  }

  @Override
  public void render(Graphics graphics, int width, int height)
  {
    graphics.clearRect(0, 0, width, height);

    for (int i = 0; i < 4000; i++)
    {
      drawRect(graphics, width, height);
    }
  }

  @Override
  public void tick(double time, GameInput input)
  {
    input.popEvents();
    if (loader.isDone())
    {
      stageManager.setStage("Title");
    }
  }

  private void drawRect(Graphics graphics, int width, int height)
  {
    graphics.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
    graphics.fillRect(random.nextInt(width - 20), random.nextInt(height - 20), 20, 20);
  }
}

