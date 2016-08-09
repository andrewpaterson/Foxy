package net.foxy.title;

import net.engine.cel.Cel;
import net.engine.cel.CelStore;
import net.engine.input.GameInput;
import net.foxy.FoxyStage;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.List;

import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;
import static net.engine.global.GlobalRandom.random;

public class TitleScreen extends FoxyStage
{
  public TitleScreen(CelStore celStore)
  {
    super(celStore);
  }

  @Override
  public void render(Graphics graphics, int width, int height)
  {
    graphics.clearRect(0, 0, width, height);

    List<Cel> cels = celStore.get("Start Text");

    Cel startCel = cels.get(0);
    graphics.drawImage(startCel.getBufferedImage(), 50, 50, null);

    graphics.setColor(Color.DARK_GRAY);
    graphics.drawRect(50, 50, startCel.getGraphicsWidth(), startCel.getGraphicsHeight());

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
            stageManager.setStage("Night One");
          }
        }
      }
    }
  }
}

