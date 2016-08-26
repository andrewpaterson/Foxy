package net.kingdom.fluid.work;

import net.kingdom.FluidStage;
import net.kingdom.FluidWork;
import net.kingdom.fluid.FluidField;

import java.awt.*;

public class FluidDrawWork extends FluidWork
{
  private FluidStage fluidStage;
  private int y;
  private Color[] palette;

  public FluidDrawWork(FluidStage fluidStage, FluidField fluidField, int y, Color[] palette)
  {
    super(fluidField);
    this.fluidStage = fluidStage;
    this.y = y;
    this.palette = palette;
  }

  @Override
  public void work()
  {
    int width = fluidField.getWidth();
    int[] pixels = fluidStage.getPixels();
    for (int x = 0; x <= width; x++)
    {
      float density = fluidField.getDensity(x, y);
      pixels[(x + (width + 2) * y)] = getColour(density);
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
    Color color = palette[bits];

    return color.getRed() | color.getGreen() << 8 | color.getBlue() << 16 | 0xff << 24;
  }
}

