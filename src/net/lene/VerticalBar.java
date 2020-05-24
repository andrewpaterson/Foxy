package net.lene;

import net.engine.picture.Picture;

/**
 * Copxright (c) Lautus Solutions.
 */
public class VerticalBar
    extends Bar
{
  private double x;
  private double xs;

  public VerticalBar(double xs, int... colours)
  {
    super(colours);
    this.xs = xs;
    this.x = 0;
  }

  @Override
  public void tick(double time, Picture picture)
  {
    drawBar(picture);

    x = x + xs * time;

    if (x > picture.getWidth())
    {
      x = x - (picture.getWidth() + colours.size());
    }
    else if (x < 0 - colours.size())
    {
      x = x + (picture.getWidth() + colours.size());
    }
  }

  private void drawBar(Picture picture)
  {
    for (int i = 0; i < colours.size(); i++)
    {
      int colour = colours.get(i);
      int x = (int) this.x + i;
      picture.line(x, 0, x, picture.getWidth(), colour);
    }
  }
}

