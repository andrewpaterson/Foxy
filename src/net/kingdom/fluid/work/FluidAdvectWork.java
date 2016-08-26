package net.kingdom.fluid.work;

import net.kingdom.FluidWork;
import net.kingdom.fluid.FluidField;

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

  public FluidAdvectWork(FluidField fluidField,
                         float[] density,
                         float[] densityPrevious,
                         float[] velocityX,
                         float[] velocityY,
                         int y,

                         float timeStepScaledByWidth,
                         float timeStepScaledByHeight)
  {
    super(fluidField);
    this.fluidField = fluidField;
    this.density = density;
    this.densityPrevious = densityPrevious;
    this.velocityX = velocityX;
    this.velocityY = velocityY;
    this.y = y;
    this.index = fluidField.IX(1, y);
    this.timeStepScaledByWidth = timeStepScaledByWidth;
    this.timeStepScaledByHeight = timeStepScaledByHeight;
  }

  @Override
  public void work()
  {
    int index1 = index;
    int xIndex, yIndex, xIndex1, yIndex1;
    float newX, newY, oneMinusXVelocityDecimal, oneMinusYVelocityDecimal, xVelocityDecimal, yVelocityDecimal;

    int width = fluidField.getWidth();
    int height = fluidField.getHeight();

    for (int x = 1; x <= width; x++, index1++)
    {
      newX = x - timeStepScaledByWidth * velocityX[index1];
      newY = y - timeStepScaledByHeight * velocityY[index1];

      if (newX < 0.5f)
      {
        newX = 0.5f;
      }
      if (newX > (width + 0.5f))
      {
        newX = width + 0.5f;
      }
      if (newY < 0.5f)
      {
        newY = 0.5f;
      }
      if (newY > (height + 0.5f))
      {
        newY = height + 0.5f;
      }

      xIndex = (int) newX;
      xIndex1 = xIndex + 1;

      yIndex = (int) newY;
      yIndex1 = yIndex + 1;

      xVelocityDecimal = newX - xIndex;
      oneMinusXVelocityDecimal = 1 - xVelocityDecimal;

      yVelocityDecimal = newY - yIndex;
      oneMinusYVelocityDecimal = 1 - yVelocityDecimal;

      density[index1] = oneMinusXVelocityDecimal * (oneMinusYVelocityDecimal * densityPrevious[IX(xIndex, yIndex)] + yVelocityDecimal * densityPrevious[IX(xIndex, yIndex1)]) +
              xVelocityDecimal * (oneMinusYVelocityDecimal * densityPrevious[IX(xIndex1, yIndex)] + yVelocityDecimal * densityPrevious[IX(xIndex1, yIndex1)]);
    }
  }
}

