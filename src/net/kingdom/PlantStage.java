package net.kingdom;

import net.engine.common.Timer;
import net.engine.game.PictureStage;
import net.engine.input.GameInput;
import net.engine.input.MouseInput;
import net.engine.math.Float2;
import net.engine.picture.Colour;
import net.engine.picture.ColourGradient;
import net.engine.picture.ComponentPicture;
import net.kingdom.plant.Tree;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class PlantStage extends PictureStage
{
  protected List<Tree> trees;

  public PlantStage(int renderWidth, int renderHeight)
  {
    super(new ComponentPicture(renderWidth, renderHeight));
    trees = new ArrayList<>();
  }

  @Override
  public void render(Graphics graphics, int windowWidth, int windowHeight)
  {
    int renderHeight = picture.getHeight();
    int renderWidth = picture.getWidth();

    Color[] colors = new Color[56];
    ColourGradient.generate(colors,
            new Color(140, 190, 255), 0,
            new Color(100, 150, 255), 20,
            new Color(50, 100, 255), 30,
            new Color(50, 255, 50), 32,
            new Color(30, 200, 30), 40,
            new Color(130, 200, 60), 45,
            new Color(105, 72, 7, 255), 50,
            new Color(72, 42, 14, 255), 55);

    float scale = 55.0f / renderHeight;

    for (Tree tree : trees)
    {
      tree.calculateBoundingBox();
    }

    Timer timer = new Timer();
    for (int y = 0; y < renderHeight; y++)
    {
      for (int x = 0; x < renderWidth; x++)
      {
        if (isInTree(x, y))
        {
          picture.unsafeSetPixel(x, y, 0xFF000000);
        }
        else
        {
          Color color = colors[(int) (y * scale)];
          picture.unsafeSetPixel(x, y, Colour.getARGB(color));
        }
      }
    }
    double rayTraceTime = timer.stop();
    renderPictureToWindow(graphics, windowWidth, windowHeight);

    String s = String.format("%.3fms", rayTraceTime);
    graphics.drawChars(s.toCharArray(), 0, s.length(), 15, 15);

  }

  private boolean isInTree(int x, int y)
  {
    Float2 position = new Float2((float) x, (float) y);
    for (Tree tree : trees)
    {
      if (tree.contains(position))
      {
        return true;
      }
    }
    return false;
  }

  @Override
  public void tick(double time, GameInput gameInput, int width, int height)
  {
    gameInput.processEvents(this, width, height);
  }

  @Override
  public void mouseInput(MouseInput input, GameInput gameInput, int width, int height)
  {
    if ((input.getButton() == 0) && (input.isPressed()))
    {
      trees.add(new Tree(new Float2(input.getX() * widthScale(width), input.getY() * heightScale(height))));
    }
  }
}

