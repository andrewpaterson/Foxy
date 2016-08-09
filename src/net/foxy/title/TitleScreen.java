package net.foxy.title;

import net.engine.cel.CelStore;
import net.engine.game.StageManager;
import net.engine.input.GameInput;
import net.engine.scene.Camera;
import net.engine.scene.Scene;
import net.engine.scene.Sprite;
import net.foxy.FoxyStage;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;
import static net.engine.global.GlobalRandom.random;

public class TitleScreen extends FoxyStage
{
  protected Scene scene = new Scene();
  protected Sprite startText;

  public TitleScreen(CelStore celStore)
  {
    super(celStore);
  }

  @Override
  public void stageStarting(StageManager stageManager)
  {
    super.stageStarting(stageManager);
    startText = new Sprite(celStore.get("Start Text").get(0));
    scene.addSprite(startText.setPosition(100, 50));
  }

  @Override
  public void render(Graphics graphics, int width, int height)
  {
    graphics.clearRect(0, 0, width, height);

    scene.render(graphics, width, height);

    renderStatic(graphics, width, height);
  }

  private void renderStatic(Graphics graphics, int width, int height)
  {
    graphics.setColor(Color.DARK_GRAY);
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
            Point point = mouseEvent.getPoint();
            Camera camera = scene.getCamera();
            Rectangle rectangle = startText.toRectangle(camera);
            if (rectangle.contains(point))
            {
              stageManager.setStage("Night One");
            }
          }
        }
      }
    }
  }
}

