package net.kingdom.fluid.draw;

import net.kingdom.FluidWork;
import net.kingdom.fluid.FluidField;

public class DrawWork extends FluidWork
{
  public int fieldWidth;
  public int y;
  public int[] pixels;

  public DrawWork(FluidField fluidField, int fieldWidth, int y, int[] pixels)
  {
    super(fluidField);
    this.fieldWidth = fieldWidth;
    this.y = y;
    this.pixels = pixels;
  }

  @Override
  public void work()
  {
    for (int x = 0; x <= fieldWidth; x++)
    {
      float density = fluidField.getDensity(x, y);
      pixels[(x + (fieldWidth + 2) * y)] = getColour(density);
    }
  }

  private int getColour(float colour)
  {
    if (colour < 0)
    {
      colour = 0;
    }
    if (colour > 1)
    {
      colour = 1;
    }
    int bits = (int) (colour * 255);
    return bits | bits << 8 | bits << 16 | 0xff << 24;
  }
}

