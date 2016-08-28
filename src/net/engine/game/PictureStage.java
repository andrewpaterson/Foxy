package net.engine.game;

import net.engine.picture.Picture;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class PictureStage extends Stage
{
  protected Picture frameBuffer;
  protected BufferedImage bufferedImage;

  protected float widthScale;
  protected float heightScale;

  public PictureStage(int renderWidth, int renderHeight, int windowWidth, int windowHeight)
  {
    this.frameBuffer = new Picture(renderWidth, renderHeight);
    bufferedImage = new BufferedImage(renderWidth, renderHeight, BufferedImage.TYPE_INT_ARGB);

    widthScale = (float) renderWidth / (float) windowWidth;
    heightScale = (float) renderHeight / (float) windowHeight;
  }

  protected void renderPictureToWindow(Graphics graphics, int windowWidth, int windowHeight)
  {
    convertPalettePictureToImageRaster(frameBuffer, bufferedImage);
    graphics.drawImage(bufferedImage, 0, 0, windowWidth, windowHeight, 0, 0, frameBuffer.getWidth(), frameBuffer.getHeight(), null);
  }
}

