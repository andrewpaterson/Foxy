package net.lene;

import net.engine.picture.Picture;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Lautus Solutions.
 */
public class HorizontalBar extends Bar {
  private double y;
  private double ys;

  public HorizontalBar(double ys, int... colours)
  {
    super(colours);
    this.ys = ys;
    this.y = 0;
  }

  @Override
  public void tick(double time, Picture picture)
  {
    drawBar(picture);

    y = y + ys * time;

    if (y > picture.getHeight())
    {
      y = y - (picture.getHeight() + colours.size());
    }
  }

  private void drawBar(Picture picture)
  {
    for (int i = 0; i < colours.size(); i++)
    {
      int colour = colours.get(i);
      int y = (int) this.y + i;
      picture.line(0, y, picture.getWidth(), y, colour);
    }
  }
}

