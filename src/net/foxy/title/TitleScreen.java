package net.foxy.title;

import net.engine.common.Stage;
import net.engine.input.GameInput;

import java.awt.*;

public class TitleScreen extends Stage
{
  private Font font;

  public TitleScreen()
  {
    font = new Font("Jokerman", Font.PLAIN, 35);
  }

  @Override
  public void render(Graphics graphics, int width, int height)
  {
    graphics.clearRect(0, 0, width, height);

    String start = "Start";
    graphics.setFont(font);
    graphics.drawChars(start.toCharArray(), 0, start.length(), 100, 100);
  }

  @Override
  public void tick(double time, GameInput input)
  {
  }
}

