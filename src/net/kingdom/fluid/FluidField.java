package net.kingdom.fluid;

import net.engine.thread.Threadanator;
import net.kingdom.fluid.advect.FluidAdvect1;
import net.kingdom.fluid.advect.FluidAdvectParams;
import net.kingdom.fluid.diffuse.FluidDiffuse1;
import net.kingdom.fluid.diffuse.FluidDiffuseParams;
import net.kingdom.fluid.project.FluidProject1;
import net.kingdom.fluid.project.FluidProject2;
import net.kingdom.fluid.project.FluidProject3;
import net.kingdom.fluid.project.FluidProjectParams;

import java.util.Arrays;

public class FluidField
{
  // Number of columns and rows in our system
  private int width;
  private int stride;
  private int height;

  private float[] velocityX;
  private float[] velocityY;
  private float[] velocityPreviousX;
  private float[] velocityPreviousY;

  private float[] density;
  private float[] densityPrevious;

  private float timeStep;
  private float viscosity;
  private float diffusionRate;
  private int velocityIterations;
  private int densityIterations;

  public FluidField(int width,
                    int height,
                    float timeStep,
                    float viscosity,
                    float diffusionRate,
                    int velocityIterations,
                    int densityIterations)
  {
    this.width = width;
    this.stride = width + 2;
    this.height = height;
    this.timeStep = timeStep;
    this.viscosity = viscosity;
    this.diffusionRate = diffusionRate;
    this.velocityIterations = velocityIterations;
    this.densityIterations = densityIterations;

    int size = (stride) * (height + 2);

    velocityX = new float[size];
    velocityY = new float[size];
    velocityPreviousX = new float[size];
    velocityPreviousY = new float[size];

    density = new float[size];
    densityPrevious = new float[size];

    clearData();
  }

  public void clearData()
  {
    Arrays.fill(velocityX, 0.0f);
    Arrays.fill(velocityY, 0.0f);
    Arrays.fill(velocityPreviousX, 0.0f);
    Arrays.fill(velocityPreviousY, 0.0f);
    Arrays.fill(density, 0.0f);
    Arrays.fill(densityPrevious, 0.0f);
  }

  int IX(int x, int y)
  {
    return x + stride * y;
  }

  /**
   * Sets boundary for diffusion. It is bound vertically and horizontally in a box.
   **/

  void setBnd(int width, int height, int boundaryHack, float[] destination)
  {
//    int i;
//
//    if (boundaryHack == 0)
//    {
//      for (i = 0; i <= width; i++)
//      {
//        destination[IX(0, i)] = destination[IX(1, i)];
//        destination[IX(width + 1, i)] = destination[IX(width, i)];
//        destination[IX(i, 0)] = destination[IX(i, 1)];
//        destination[IX(i, width + 1)] = destination[IX(i, width)];
//      }
//    }
//
//    if (boundaryHack == 1)
//    {
//      for (i = 0; i <= width; i++)
//      {
//        destination[IX(0, i)] = -destination[IX(1, i)];
//        destination[IX(width + 1, i)] = -destination[IX(width, i)];
//        destination[IX(i, 0)] = destination[IX(i, 1)];
//        destination[IX(i, width + 1)] = destination[IX(i, width)];
//      }
//    }
//
//    if (boundaryHack == 2)
//    {
//      for (i = 0; i <= width; i++)
//      {
//        destination[IX(0, i)] = destination[IX(1, i)];
//        destination[IX(width + 1, i)] = destination[IX(width, i)];
//        destination[IX(i, 0)] = -destination[IX(i, 1)];
//        destination[IX(i, width + 1)] = -destination[IX(i, width)];
//      }
//    }
//
//    destination[IX(0, 0)] = 0.5f * (destination[IX(1, 0)] + destination[IX(0, 1)]);
//    destination[IX(0, width + 1)] = 0.5f * (destination[IX(1, width + 1)] + destination[IX(0, width)]);
//    destination[IX(width + 1, 0)] = 0.5f * (destination[IX(width, 0)] + destination[IX(width + 1, 1)]);
//    destination[IX(width + 1, width + 1)] = 0.5f * (destination[IX(width, width + 1)] + destination[IX(width + 1, width)]);
  }

  void diffuse(int width,
               int height,
               int boundaryHack,
               float[] destination,
               float[] source,
               float diffusionRate,
               float timeStep,
               int iterations)
  {
    if (diffusionRate != 0)
    {
      float a = timeStep * diffusionRate * width * height;
      diffuse(new FluidDiffuseParams(destination, source, a), boundaryHack, iterations);
    }
    else
    {
      copy(boundaryHack, destination, source);
    }
  }

  private void diffuse(FluidDiffuseParams params,
                       int boundaryHack,
                       int iterations)
  {
    float constant = 1.0f / (1.0f + (4 * params.a));
    Threadanator threadanator = Threadanator.getInstance();

    for (int i = 0; i < iterations; i++)
    {
      for (int y = 1; y <= height; y++)
      {
        int index = IX(1, y);
        threadanator.add(new FluidDiffuse1(this, params, constant, index));
      }
      threadanator.join();

      setBnd(width, height, boundaryHack, params.destination);
    }
  }

  public void diffuse1(FluidDiffuseParams params, float constant, int index)
  {
    for (int x = 1; x <= width; x++, index++)
    {
      float adjacentValueSum = sumAdjacentValues(index, params.destination);
      params.destination[index] = constant * (params.source[index] + params.a * adjacentValueSum);
    }
  }

  private void copy(int boundaryHack,
                    float[] destination,
                    float[] source)
  {
    System.arraycopy(source, 0, destination, 0, (stride) * (height + 2));
    setBnd(width, height, boundaryHack, destination);
  }

  void advect(FluidAdvectParams params, int boundaryHack)
  {
    Threadanator threadanator = Threadanator.getInstance();

    float timeStepScaledByWidth = params.dt * width;
    float timeStepScaledByHeight = params.dt * height;

    for (int y = 1; y <= height; y++)
    {
      int index = IX(1, y);

      threadanator.add(new FluidAdvect1(this, params, y, index, timeStepScaledByWidth, timeStepScaledByHeight));
      advect1(params, y, index, timeStepScaledByWidth, timeStepScaledByHeight);
    }
    threadanator.join();

    setBnd(width, height, boundaryHack, density);
  }

  public void advect1(FluidAdvectParams params, int y, int index, float timeStepScaledByWidth, float timeStepScaledByHeight)
  {
    int xIndex, yIndex, xIndex1, yIndex1;
    float newX, newY, oneMinusXVelocityDecimal, oneMinusYVelocityDecimal, xVelocityDecimal, yVelocityDecimal;

    for (int x = 1; x <= width; x++, index++)
    {
      newX = x - timeStepScaledByWidth * params.velocityX[index];
      newY = y - timeStepScaledByHeight * params.velocityY[index];

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

      params.density[index] = oneMinusXVelocityDecimal * (oneMinusYVelocityDecimal * params.densityPrevious[IX(xIndex, yIndex)] + yVelocityDecimal * params.densityPrevious[IX(xIndex, yIndex1)]) +
              xVelocityDecimal * (oneMinusYVelocityDecimal * params.densityPrevious[IX(xIndex1, yIndex)] + yVelocityDecimal * params.densityPrevious[IX(xIndex1, yIndex1)]);
    }
  }

  void calculateDensity(float[] density,
                        float[] densityPrevious,
                        float[] velocityX,
                        float[] velocityY,
                        float diffusionRate,
                        float timeStep,
                        int iterations)
  {
    addTimeScaled(density, densityPrevious, timeStep);

    diffuse(width, height, 0, densityPrevious, density, diffusionRate, timeStep, iterations);

    advect(new FluidAdvectParams(density, densityPrevious, velocityX, velocityY, timeStep), 0);
  }

  /**
   * 1 step of the velocity solver.
   */
  void calculateVelocity(float[] velocityX,
                         float[] velocityY,
                         float[] velocityPreviousX,
                         float[] velocityPreviousY,
                         float viscosity,
                         float timeStep,
                         int iterations)
  {
    addTimeScaled(velocityX, velocityPreviousX, timeStep);
    addTimeScaled(velocityY, velocityPreviousY, timeStep);

    diffuse(width, height, 1, velocityPreviousX, velocityX, viscosity, timeStep, 10);
    diffuse(width, height, 2, velocityPreviousY, velocityY, viscosity, timeStep, 10);

    project(new FluidProjectParams(velocityPreviousX, velocityPreviousY, velocityX, velocityY), iterations);

    advect(new FluidAdvectParams(velocityX, velocityPreviousX, velocityPreviousX, velocityPreviousY, timeStep), 1);
    advect(new FluidAdvectParams(velocityY, velocityPreviousY, velocityPreviousX, velocityPreviousY, timeStep), 2);

    project(new FluidProjectParams(velocityX, velocityY, velocityPreviousX, velocityPreviousY), iterations);
  }

  void project(FluidProjectParams params, int iterations)
  {
    float h = 1.0f / width;
    float halfHNegative = -0.5f * h;
    float halfNNegative = -0.5f * width;

    Threadanator threadanator = Threadanator.getInstance();

    for (int y = 1; y <= height; y++)
    {
      int index = IX(1, y);
      threadanator.add(new FluidProject1(this, params, halfHNegative, index));
    }
    threadanator.join();

    Arrays.fill(params.sourceVelocityX, 0);

    setBnd(width, height, 0, params.sourceVelocityY);
    setBnd(width, height, 0, params.sourceVelocityX);

    for (int i = 0; i < iterations; i++)
    {
      for (int y = 1; y <= height; y++)
      {
        int index = IX(1, y);
        threadanator.add(new FluidProject2(this, params, index));
      }
      threadanator.join();

      setBnd(width, height, 0, params.sourceVelocityX);
    }

    for (int y = 1; y <= height; y++)
    {
      int index = IX(1, y);
      threadanator.add(new FluidProject3(this, params, halfNNegative, index));
    }
    threadanator.join();

    setBnd(width, height, 1, params.destinationVelocityX);
    setBnd(width, height, 2, params.destinationVelocityY);
  }

  public void project1(FluidProjectParams params, float halfHNegative, int index)
  {
    for (int x = 1; x <= width; x++, index++)
    {
      params.sourceVelocityY[index] = halfHNegative * (params.destinationVelocityX[index + 1] - params.destinationVelocityX[index - 1] + params.destinationVelocityY[index + stride] - params.destinationVelocityY[index - stride]);
    }
  }

  public void project2(FluidProjectParams params, int index)
  {
    for (int x = 1; x <= width; x++, index++)
    {
      params.sourceVelocityX[index] = 0.25f * (params.sourceVelocityY[index] + sumAdjacentValues(index, params.sourceVelocityX));
    }
  }

  public void project3(FluidProjectParams params, float halfNNegative, int index)
  {
    for (int x = 1; x <= width; x++, index++)
    {
      params.destinationVelocityX[index] += halfNNegative * (params.sourceVelocityX[index + 1] - params.sourceVelocityX[index - 1]);
      params.destinationVelocityY[index] += halfNNegative * (params.sourceVelocityX[index + stride] - params.sourceVelocityX[index - stride]);
    }
  }

  private float sumAdjacentValues(int index, float[] values)
  {
    return values[index - 1] + values[index + 1] + values[index - stride] + values[index + stride];
  }

  public int getWidth()
  {
    return width;
  }

  public int getHeight()
  {
    return height;
  }

  public float getDensity(int x, int y)
  {
    return density[IX(x, y)];
  }

  public void setForce(int i, int j, float u, float v)
  {
    this.velocityX[IX(i, j)] = u;
    this.velocityY[IX(i, j)] = v;

  }

  public void setDensity(int i, int j, float source)
  {
    densityPrevious[IX(i, j)] = source;
  }

  public void clearPrevious()
  {
    int size = stride * (height + 2);
    for (int i = 0; i < size; i++)
    {
      velocityPreviousX[i] = 0.0f;
      velocityPreviousY[i] = 0.0f;
      densityPrevious[i] = 0.0f;
    }
  }

  void addTimeScaled(float[] destination, float[] source, float timeStep)
  {
    int i;
    int size = (stride) * (height + 2);

    for (i = 0; i < size; i++)
    {
      destination[i] += timeStep * source[i];
    }
  }

  public void tick()
  {
    long startTime = System.nanoTime();

    calculateVelocity(velocityX, velocityY, velocityPreviousX, velocityPreviousY, viscosity, timeStep, velocityIterations);
    calculateDensity(density, densityPrevious, velocityX, velocityY, diffusionRate, timeStep, densityIterations);

    long endTime = System.nanoTime();
    double timeInSeconds = (double) (endTime - startTime) / 1000000000;
    System.out.println(String.format("%.3f", timeInSeconds));
  }
}

