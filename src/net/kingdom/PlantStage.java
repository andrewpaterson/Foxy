package net.kingdom;

import net.engine.common.Timer;
import net.engine.game.PictureStage;
import net.engine.input.GameInput;
import net.engine.input.MouseInput;
import net.engine.input.PointerLocation;
import net.engine.math.Float2;
import net.engine.picture.Colour;
import net.engine.picture.ColourGradient;
import net.engine.picture.ComponentPicture;
import net.kingdom.plant.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class PlantStage extends PictureStage
{
  protected List<Tree> trees;
  protected RayScene rayScene;
  protected Float2 light;

  public PlantStage(int renderWidth, int renderHeight)
  {
    super(new ComponentPicture(renderWidth, renderHeight));
    trees = new ArrayList<>();
    rayScene = new RayScene(renderWidth, renderHeight, 8, 8);
    light = new Float2();
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
    RayResult closestRayResult = null;
    int objects = block.objectSize();
    for (int i = 0; i < objects; i++)
    {
      RaycastObject raycastObject = block.raycastObjects.get(i);
      if (raycastObject.probablyContains(x, y))
      {
        RayResult rayResult = raycastObject.cast(x, y);
        if (rayResult != null)
        {
          if (closestRayResult == null)
          {
            closestRayResult = rayResult;
          }
          else
          {
            if (closestRayResult.z > rayResult.z)
            {
              closestRayResult = rayResult;
            }
          }
        }
      }
    }

    if (closestRayResult == null)
    {
      Color color = colors[(int) (y * scale)];
      picture.unsafeSetPixel(x, y, Colour.getARGB(color));
    }
    else
    {
      float brightness = closestRayResult.normal.z;
      int colour = Colour.getARGB(1, brightness, brightness, brightness);
      picture.unsafeSetPixel(x, y, colour);
    }
  }

  @Override
  public void tick(double time, GameInput gameInput, int width, int height)
  {
    gameInput.processEvents(this, width, height);
    PointerLocation pointerLocation = gameInput.getPointerLocation();
    if (pointerLocation != null)
    {
      light.set(pointerLocation.getX(), pointerLocation.getY());
    }
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

