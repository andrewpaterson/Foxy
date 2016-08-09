package net.foxy.loading;

import net.engine.common.Stage;

import java.awt.*;

import static net.engine.common.GameRandom.random;

public class LoadingScreen extends Stage
{
  @Override
  public void render(Graphics graphics, int width, int height)
  {
    graphics.clearRect(0, 0, width, height);

    for (int i = 0; i < 10000; i++)
    {
      drawRect(graphics, width, height);
    }
  }

  @Override
  public void tick(double time)
  {
  }

  private void drawRect(Graphics graphics, int width, int height)
  {
    graphics.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
    graphics.fillRect(random.nextInt(width - 20), random.nextInt(height - 20), 20, 20);
  }
}

