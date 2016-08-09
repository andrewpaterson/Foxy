package net.foxy.title;

import net.engine.common.Stage;
import net.engine.input.GameInput;
import net.foxy.nights.NightOne;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;

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
            stageManager.setStage(new NightOne());
          }
        }
      }
    }
  }
}

