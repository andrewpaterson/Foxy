package net.foxy.title;

import net.engine.cel.CelStore;
import net.engine.game.StageManager;
import net.engine.input.*;
import net.engine.scene.SpriteMap;
import net.engine.scene.Sprite;
import net.foxy.FoxyStage;

import java.awt.*;

import static net.engine.global.GlobalRandom.random;

public class TitleScreen extends FoxyStage implements InputHandler
{
  protected SpriteMap spriteMap = new SpriteMap();
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
    spriteMap.addSprite(startText.setPosition(100, 50));
  }

  @Override
  public void render(Graphics graphics, int width, int height)
  {
    graphics.clearRect(0, 0, width, height);

    spriteMap.render(graphics, width, height);

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
  public void tick(double time, GameInput input, int width, int height)
  {
    input.processEvents(this, width, height);
  }
}

