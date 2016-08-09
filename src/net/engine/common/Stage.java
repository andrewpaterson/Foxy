package net.engine.common;

import java.awt.*;

public abstract class Stage
{
  public abstract void render(Graphics graphics, int width, int height);

  public abstract void tick(double time);
}

