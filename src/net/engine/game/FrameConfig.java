package net.engine.game;

import net.engine.global.GlobalGraphics;

import java.awt.*;

public class FrameConfig
{
  public String title;

  public int width;
  public int height;
  public int left;
  public int top;
  public boolean decorated;
  public boolean onTop;
  public boolean fullScreen;

  public FrameConfig(String title, int width, int height)
  {
    this.title = title;
    this.width = width;
    this.height = height;
    this.decorated = true;
    this.onTop = false;
    this.fullScreen = false;

    center(width, height);
  }

  public FrameConfig(String title, int width, int height, boolean fullScreen)
  {
    this.title = title;
    this.width = width;
    this.height = height;
    this.decorated = false;
    this.onTop = false;
    this.fullScreen = fullScreen;
    this.top = 0;
    this.left = 0;
  }

  public FrameConfig(String title, float size)
  {
    this.title = title;
    this.decorated = true;
    this.onTop = false;
    this.fullScreen = false;

    scale(size);
    center(width, height);
  }

  public FrameConfig(String title)
  {
    this.title = title;
    Rectangle bounds = GlobalGraphics.getGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
    this.width = (int) bounds.getWidth();
    this.height = (int) bounds.getHeight();
    this.decorated = false;
    this.onTop = false;
    this.fullScreen = false;

    center(width, height);
  }

  private void scale(float size)
  {
    Rectangle bounds = GlobalGraphics.getGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
    this.width = (int) (bounds.getWidth() * size);
    this.height = (int) (bounds.getHeight() * size);
  }

  private void center(int width, int height)
  {
    Rectangle bounds = GlobalGraphics.getGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
    this.left = (int) ((bounds.getWidth() - width) / 2);
    this.top = (int) ((bounds.getHeight() - height) / 2);
  }
}

