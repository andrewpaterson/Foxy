package net.kingdom;

import net.engine.game.PictureStage;
import net.engine.input.GameInput;
import net.engine.input.MouseInput;
import net.engine.math.Float2;
import net.engine.shape.Capsule;
import net.kingdom.plant.Tree;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class PlantStage extends PictureStage
{
  protected List<Tree> trees;

  public PlantStage(int renderWidth, int renderHeight, int windowWidth, int windowHeight)
  {
    super(renderWidth, renderHeight, windowWidth, windowHeight);
    trees = new ArrayList<>();
  }

  @Override
  public void render(Graphics graphics, int windowWidth, int windowHeight)
  {
    int renderHeight = frameBuffer.getHeight();
    int renderWidth = frameBuffer.getWidth();

    frameBuffer.setPaletteFromColourGradient(
            new Color(140, 190, 255), 0,
            new Color(100, 150, 255), 50,
            new Color(50, 100, 255), 160,
            new Color(50, 255, 50), 162,
            new Color(30, 200, 30), 180,
            new Color(130, 200, 60), 200,
            new Color(105, 72, 7, 255), 220,
            new Color(72, 42, 14, 255), 240,
            new Color(198, 183, 42, 255), 255);
    float scale = (float) 255 / (float) renderHeight;
    for (int y = 0; y < renderHeight; y++)
    {
      for (int x = 0; x < renderWidth; x++)
      {
        frameBuffer.setPixel(x, y, (int) (y * scale));
      }
    }
    frameBuffer.speckle(1);

    for (Tree tree : trees)
    {
      for (Capsule branch : tree.branches)
      {
        Float2 start = branch.getStart();
        Float2 end = branch.getEnd();
        frameBuffer.line((int) start.x, (int) start.y, (int) end.x, (int) end.y, 240);
      }
    }

    renderPictureToWindow(graphics, windowWidth, windowHeight);
  }

  @Override
  public void tick(double time, GameInput gameInput)
  {
    gameInput.processEvents(this);
  }

  @Override
  public void mouseInput(MouseInput input)
  {
    super.mouseInput(input);
    if ((input.getButton() == 0) && (input.isPressed()))
    {
      trees.add(new Tree(new Float2(input.getX() * widthScale, input.getY() * heightScale)));
    }
  }
}

