package net.kingdom.fluid.work;

import net.kingdom.fluid.FluidField;
import net.kingdom.FluidWork;

public class FluidAdvectWork extends FluidWork
{
  private final FluidField fluidField;
  private int y;
  private int index;
  private float timeStepScaledByWidth;
  private float timeStepScaledByHeight;
  private float[] density;
  private float[] densityPrevious;
  private float[] velocityX;
  private float[] velocityY;
  private float timeStep;

  public FluidAdvectWork(FluidField fluidField, float[] density, float[] densityPrevious, float[] velocityX, float[] velocityY, float timeStep, int y, int index, float timeStepScaledByWidth, float timeStepScaledByHeight)
  {
    super(fluidField);
    this.fluidField = fluidField;
    this.density = density;
    this.densityPrevious = densityPrevious;
    this.velocityX = velocityX;
    this.velocityY = velocityY;
    this.timeStep = timeStep;
    this.y = y;
    this.index = index;
    this.timeStepScaledByWidth = timeStepScaledByWidth;
    this.timeStepScaledByHeight = timeStepScaledByHeight;
  }

  @Override
  public void work()
  {
    fluidField.advect1(density, densityPrevious, velocityX, velocityY, timeStep, y, index, timeStepScaledByWidth, timeStepScaledByHeight);
  }
}

