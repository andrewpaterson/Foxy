package net.foxy.nights;

import net.engine.cel.CelStore;
import net.engine.input.GameInput;
import net.foxy.FoxyStage;

import java.awt.*;

public class NightOne
    extends FoxyStage
{
  protected float brightness;
  protected float direction;

  public NightOne(CelStore celStore)
  {
    super(celStore);
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
  public void tick(double time, GameInput input, int width, int height)
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

