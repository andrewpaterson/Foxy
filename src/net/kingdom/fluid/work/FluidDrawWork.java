package net.kingdom.fluid.work;

import net.kingdom.FluidStage;
import net.kingdom.fluid.FluidField;

public class FluidDrawWork extends FluidWork
{
  private FluidStage fluidStage;
  private int y;

  public FluidDrawWork(FluidStage fluidStage, FluidField fluidField, int y)
  {
    super(fluidField);
    this.fluidStage = fluidStage;
    this.y = y;
  }

  @Override
  public void work()
  {
    int width = fluidField.getWidth();
    byte[] pixels = fluidStage.getPixels();
    for (int x = 0; x <= width; x++)
    {
      float density = fluidField.getDensity(x, y);
      pixels[(x + (width + 2) * y)] = getColour(density);
    }
  }

  private byte getColour(float colour)
  {
    if (colour < 0)
    {
      colour = 0;
    }
    if (colour > 1)
    {
      colour = 1;
    }

    return (byte) (colour * 255 - 128);
  }
}

