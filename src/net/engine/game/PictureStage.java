package net.engine.game;

import net.engine.picture.Picture;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class PictureStage extends Stage
{
  protected Picture frameBuffer;
  protected BufferedImage bufferedImage;

  public PictureStage(int renderWidth, int renderHeight)
  {
    frameBuffer = new Picture(renderWidth, renderHeight);
    bufferedImage = new BufferedImage(renderWidth, renderHeight, BufferedImage.TYPE_INT_ARGB);

  }

  protected void renderPictureToWindow(Graphics graphics, int windowWidth, int windowHeight)
  {
    convertPalettePictureToImageRaster(frameBuffer, bufferedImage);
    graphics.drawImage(bufferedImage, 0, 0, windowWidth, windowHeight, 0, 0, frameBuffer.getWidth(), frameBuffer.getHeight(), null);
  }

  protected float widthScale(float windowWidth)
  {
    return frameBuffer.getWidth() / windowWidth;
  }

  protected float heightScale(float windowHeight)
  {
    return frameBuffer.getHeight() / windowHeight;
  }
}

