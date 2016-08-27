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
    renderStaticDots(graphics, width, height);
    renderStaticLines(graphics, width, height);

    int bars = random.nextInt(5);
    if (bars == 1)
    {
      renderStaticBar(graphics, width, height);
    }
    else if (bars == 0)
    {
      renderStaticBar(graphics, width, height);
      renderStaticBar(graphics, width, height);
      renderStaticBar(graphics, width, height);
    }
  }

  private void renderStaticBar(Graphics graphics, int width, int height)
  {
    int barHeight = random.nextInt(20) + 5;
    int top = random.nextInt(height - barHeight);
    for (int y = 0; y < barHeight; y++)
    {
      int type = random.nextInt(10);
      if (type > 1)
      {
        if ((type >= 2) && type <= 4)
        {
          graphics.setColor(Color.BLACK);
        }
        else
        {
          graphics.setColor(Color.GRAY);
        }
        graphics.drawLine(0, y + top, width, y + top);
      }
    }
  }

  private void renderStaticLines(Graphics graphics, int width, int height)
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

  private void renderStaticDots(Graphics graphics, int width, int height)
  {
    graphics.setColor(Color.LIGHT_GRAY);
    for (int i = 0; i < 2000; i++)
    {
      int x = random.nextInt(width);
      int y = random.nextInt(height);
      graphics.drawLine(x - 1, y, x + 1, y);
    }
  }

  @Override
  public void tick(double time, GameInput input)
  {
    input.processEvents(null);
  }
}

