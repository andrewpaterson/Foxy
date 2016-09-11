package net.kingdom;

import net.engine.common.Timer;
import net.engine.game.PictureStage;
import net.engine.input.GameInput;
import net.engine.input.KeyInput;
import net.engine.input.MouseInput;
import net.engine.input.PointerLocation;
import net.engine.math.Float2;
import net.engine.picture.Colour;
import net.engine.picture.ColourGradient;
import net.engine.picture.ComponentPicture;
import net.kingdom.plant.*;
import net.kingdom.plant.structure.PlantNode;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static net.kingdom.plant.structure.PlantNode.SUGAR;
import static net.kingdom.plant.structure.PlantNode.WATER;


public class PlantStage extends PictureStage implements TreeDebug
{
  protected List<Tree> trees;
  protected RayScene rayScene;
  protected Float2 light;
  protected int debugType;

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

    Color[] colors = new Color[256];
    ColourGradient.generate(colors,
            new Color(140, 190, 255), 0,
            new Color(100, 150, 255), 120,
            new Color(50, 100, 255), 205,
            new Color(50, 255, 50), 210,
            new Color(30, 200, 30), 220,
            new Color(130, 200, 60), 230,
            new Color(105, 72, 7, 255), 240,
            new Color(72, 42, 14, 255), 255);

    float scale = 255.0f / renderHeight;

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
      float brightness = closestRayResult.normal.z * 0.5f + 0.5f;
      int colour = Colour.getARGB(1,
              brightness * ((float) closestRayResult.color.getRed() / 255),
              brightness * ((float) closestRayResult.color.getGreen() / 255),
              brightness * ((float) closestRayResult.color.getBlue() / 255));
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

    for (Tree tree : trees)
    {
      tree.grow();

      tree.water(0.5f);
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
    Tree tree = new Tree(new Float2(x, y), this);
    trees.add(tree);

    rayScene.add(tree);
  }

  @Override
  public void keyInput(KeyInput input, GameInput gameInput, int width, int height)
  {
    if (input.getKey() == KeyEvent.VK_C)
    {
      trees.clear();
      rayScene.clear();
    }
    else if (input.getKey() == KeyEvent.VK_1)
    {
      debugType = 0;
    }
    else if (input.getKey() == KeyEvent.VK_2)
    {
      debugType = 1;
    }
    else if (input.getKey() == KeyEvent.VK_3)
    {
      debugType = 2;
    }
  }

  @Override
  public Color getDebugColour(PlantNode plantNode)
  {
    if (debugType == 0)
    {
      return plantNode.getDebugColour();
    }
    else if (debugType == 1)
    {
      float value = plantNode.getValue(WATER);
      return getBlueColor(value);
    }
    else if (debugType == 2)
    {
      float value = plantNode.getValue(SUGAR);
      value /= 5;
      return getGreenColor(value);
    }
    else
    {
      return Color.YELLOW;
    }
  }

  private Color getBlueColor(float value)
  {
    if (value < 0)
    {
      return Color.RED;
    }
    float major = calculateMajorColour(value);
    float minor = calculateMinorColour(value);
    return new Color(minor, minor, major);
  }

  private Color getGreenColor(float value)
  {
    if (value < 0)
    {
      return Color.RED;
    }

    float major = calculateMajorColour(value);
    float minor = calculateMinorColour(value);
    return new Color(minor, major, minor);
  }

  private float calculateMajorColour(float value)
  {
    float major = 1;
    if (value <= 1.0f)
    {
      major = value;
    }
    else if (value <= 2.0f)
    {
      major = 1;
    }
    return major;
  }

  private float calculateMinorColour(float value)
  {
    float minor = 1;
    if (value <= 1.0f)
    {
      minor = 0.08f - ((1.0f - value) * 0.08f);
    }
    else if (value <= 2.0f)
    {
      minor = value / 2;
    }
    return minor;
  }
}

