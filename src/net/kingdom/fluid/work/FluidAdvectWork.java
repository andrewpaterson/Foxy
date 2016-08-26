package net.kingdom.fluid.work;

import net.kingdom.fluid.FluidField;
import net.kingdom.FluidWork;

public class FluidAdvectWork extends FluidWork
{
  private final FluidField fluidField;
  private FluidAdvectParams params;
  private final int y;
  private final int index;
  private final float timeStepScaledByWidth;
  private final float timeStepScaledByHeight;

  public FluidAdvectWork(FluidField fluidField, FluidAdvectParams params, int y, int index, float timeStepScaledByWidth, float timeStepScaledByHeight)
  {
    super(fluidField);
    this.fluidField = fluidField;
    this.params = params;
    this.y = y;
    this.index = index;
    this.timeStepScaledByWidth = timeStepScaledByWidth;
    this.timeStepScaledByHeight = timeStepScaledByHeight;
  }

  @Override
  public void work()
  {
    fluidField.advect1(params, y, index, timeStepScaledByWidth, timeStepScaledByHeight);
  }
}
