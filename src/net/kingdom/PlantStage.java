package net.kingdom;

import net.engine.game.Stage;
import net.engine.game.StageManager;
import net.engine.input.GameInput;
import net.engine.picture.Picture;

import java.awt.*;
import java.awt.image.BufferedImage;


public class PlantStage extends Stage
{
  private Picture picture;

  public PlantStage(int renderWidth, int renderHeight)
  {
    this.picture = new Picture(renderWidth, renderHeight);
    picture.setPaletteFromColourGradient(
            new Color(0, 0, 0), 0,
            new Color(99, 44, 255), 50,
            new Color(237, 0, 56), 100,
            new Color(237, 0, 201), 150,
            new Color(237, 100, 201), 200,
            new Color(255, 253, 75), 240,
            new Color(255, 255, 255), 255);
    float scale = (float) 255 / (float) renderHeight;
    for (int y = 0; y < renderHeight; y++)
    {
      for (int x = 0; x < renderWidth; x++)
      {
        picture.setPixel(x, y, (int) (y * scale));
      }
    }
    picture.speckle(3);
  }

  @Override
  public void stageStarting(StageManager stageManager)
  {
    super.stageStarting(stageManager);
    bufferedImage = new BufferedImage(picture.getWidth(), picture.getHeight(), BufferedImage.TYPE_INT_ARGB);
  }

  protected BufferedImage bufferedImage = null;

  @Override
  public void render(Graphics graphics, int windowWidth, int windowHeight)
  {
    convertPalettePictureToImageRaster(picture, bufferedImage);
    graphics.drawImage(bufferedImage, 0, 0, windowWidth, windowHeight, 0, 0, picture.getWidth(), picture.getHeight(), null);
  }

  @Override
  public void tick(double time, GameInput gameInput)
  {
    gameInput.processEvents(this);
  }
}

