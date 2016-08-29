package net.kingdom;

import net.engine.common.Timer;
import net.engine.game.PictureStage;
import net.engine.global.GlobalRandom;
import net.engine.input.GameInput;
import net.engine.input.MouseInput;
import net.engine.math.Float2;
import net.engine.picture.Colour;
import net.engine.picture.ColourGradient;
import net.engine.picture.ComponentPicture;
import net.kingdom.plant.RayBlock;
import net.kingdom.plant.RayScene;
import net.kingdom.plant.RaycastObject;
import net.kingdom.plant.Tree;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class PlantStage extends PictureStage
{
  protected List<Tree> trees;
  protected RayScene rayScene;

  public PlantStage(int renderWidth, int renderHeight)
  {
    super(new ComponentPicture(renderWidth, renderHeight));
    trees = new ArrayList<>();
    rayScene = new RayScene(renderWidth, renderHeight, 8, 8);
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

    Timer timer = new Timer();

    rayScene.calculateOverlaps();
    RayBlock[] blocks = rayScene.getBlocks();
    for (RayBlock block : blocks)
    {
      for (int y = block.top; y < block.bottom; y++)
      {
        for (int x = block.left; x < block.right; x++)
        {
          renderBlock(colors, scale, block, x, y);
        }
      }
    }

    double rayTraceTime = timer.stop();
    renderPictureToWindow(graphics, windowWidth, windowHeight);

    String s = String.format("%.3fms", rayTraceTime);
    graphics.drawChars(s.toCharArray(), 0, s.length(), 15, 15);

  }

  private void renderBlock(Color[] colors, float scale, RayBlock block, int x, int y)
  {
    boolean isBackground = true;
    int objects = block.objectSize();
    for (int i = 0; i < objects; i++)
    {
      RaycastObject raycastObject = block.raycastObjects.get(i);
      if (raycastObject.contains(x, y))
      {
        isBackground = false;
      }
    }

    if (isBackground)
    {
      Color color = colors[(int) (y * scale)];
      picture.unsafeSetPixel(x, y, Colour.getARGB(color));
    }
    else
    {
      picture.unsafeSetPixel(x, y, 0xFF000000);
    }
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
      addTree(input.getX() * widthScale(width), input.getY() * heightScale(height));
    }
  }

  private void addTree(float x, float y)
  {
    Tree tree = new Tree(new Float2(x, y));
    trees.add(tree);

    rayScene.add(tree);
  }
}

