package net.foxy.title;

import net.engine.common.Stage;
import net.engine.input.GameInput;
import net.foxy.nights.NightOne;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;
import static net.engine.common.GameRandom.random;

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

    graphics.setColor(Color.DARK_GRAY);
    String start = "Start";
    graphics.setFont(font);
    graphics.drawChars(start.toCharArray(), 0, start.length(), 100, 100);
    graphics.drawRect(100, 100, 200, 50);

    for (int i = 0; i < 10; i++)
    {
      int x = random.nextInt(width - 50);
      int y = random.nextInt(height);
      int widthX = random.nextInt(100) + 50;
      graphics.drawLine(x, y, x + widthX, y);
    }
  }

  @Override
  public void tick(double time, GameInput input)
  {
    java.util.List<InputEvent> inputEvents = input.popEvents();
    for (InputEvent inputEvent : inputEvents)
    {
      if (inputEvent instanceof MouseEvent)
      {
        MouseEvent mouseEvent = (MouseEvent) inputEvent;
        if ((mouseEvent.getButton() == 1))
        {
          int modifiersEx = mouseEvent.getModifiersEx();
          if ((modifiersEx & BUTTON1_DOWN_MASK) != 0)
          {
            System.out.println(mouseEvent.getPoint());
            stageManager.setStage(new NightOne());
          }
        }
      }
    }
  }
}

