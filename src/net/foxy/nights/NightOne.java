package net.foxy.nights;

import net.engine.common.Stage;
import net.engine.input.GameInput;

import java.awt.*;

/**
 * Created by andrew on 2016/08/09.
 */
public class NightOne extends Stage
{
  protected float brightness;
  protected float direction;

  public NightOne()
  {
    brightness = 1;
    direction = -1;
  }

  @Override
  public void render(Graphics graphics, int width, int height)
  {
    graphics.setColor(new Color(brightness, brightness, brightness));
    graphics.fillRect(0, 0, width, height);
  }

  @Override
  public void tick(double time, GameInput input)
  {
    brightness = (float) (brightness + time * direction);
    if (brightness < 0)
    {
      brightness = 0;
      direction = 1;
    }
    else if (brightness > 1)
    {
      brightness = 1;
      direction = -1;
    }
  }
}

