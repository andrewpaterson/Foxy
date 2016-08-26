package net.kingdom.fluid;

import net.engine.thread.Job;
import net.engine.thread.Threadanator;
import net.engine.thread.util.CopyJob;
import net.kingdom.fluid.work.*;

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

  private Job advectDensityJob;
  private Job advectVelocityXJob;
  private Job advectVelocityYJob;

  private Job diffuseDensityJob;
  private Job diffuseVelocityXJob;
  private Job diffuseVelocityYJob;

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

    int size = stride * (height + 2);

    velocityX = new float[size];
    velocityY = new float[size];
    velocityPreviousX = new float[size];
    velocityPreviousY = new float[size];

    density = new float[size];
    densityPrevious = new float[size];

    advectDensityJob = createAdvectJob(density, densityPrevious, velocityX, velocityY, timeStep);
    advectVelocityXJob = createAdvectJob(velocityX, velocityPreviousX, velocityPreviousX, velocityPreviousY, timeStep);
    advectVelocityYJob = createAdvectJob(velocityY, velocityPreviousY, velocityPreviousX, velocityPreviousY, timeStep);

    diffuseDensityJob = createDiffuseJob(densityPrevious, density, diffusionRate);
    diffuseVelocityXJob = createDiffuseJob(velocityPreviousX, velocityX, diffusionRate);
    diffuseVelocityYJob = createDiffuseJob(velocityPreviousY, velocityY, diffusionRate);

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

  public int IX(int x, int y)
  {
    return x + stride * y;
  }

  /**
   * Sets boundary for diffusion. It is bound vertically and horizontally in a box.
   **/

  void diffuse(Job diffuseJob,
               float diffusionRate,
               int iterations)
  {
    if (diffusionRate != 0)
    {
      for (int i = 0; i < iterations; i++)
      {
        Threadanator.getInstance().processJob(diffuseJob);
      }
    }
    else
    {
      Threadanator.getInstance().processJob(diffuseJob);
    }
  }

  private Job createDiffuseJob(float[] destination, float[] source, float diffusionRate)
  {
    if (diffusionRate != 0)
    {
      float a = timeStep * diffusionRate * width * height;
      float constant = 1.0f / (1.0f + (4 * a));

      Job diffuseJob = new Job(16);
      for (int y = 1; y <= height; y++)
      {
        diffuseJob.add(new FluidDiffuseWork(this, destination, source, a, constant, y));
      }
      return diffuseJob;
    }
    else
    {
      return CopyJob.copy(destination, source);
    }
  }

  private Job createAdvectJob(float[] density, float[] densityPrevious, float[] velocityX, float[] velocityY, float timeStep)
  {
    Job job = new Job(16);
    float timeStepScaledByWidth = timeStep * width;
    float timeStepScaledByHeight = timeStep * height;

    for (int y = 1; y <= height; y++)
    {
      job.add(new FluidAdvectWork(this, density, densityPrevious, velocityX, velocityY, y, timeStepScaledByWidth, timeStepScaledByHeight));
    }

    return job;
  }

  void calculateDensity()
  {
    addTimeScaled(density, densityPrevious, stride, timeStep);

    diffuse(diffuseDensityJob, diffusionRate, densityIterations);

    Threadanator.getInstance().processJob(advectDensityJob);
  }

  /**
   * 1 step of the velocity solver.
   */
  void calculateVelocity()
  {
    addTimeScaled(velocityX, velocityPreviousX, stride, timeStep);
    addTimeScaled(velocityY, velocityPreviousY, stride, timeStep);

    diffuse(diffuseVelocityXJob, viscosity, velocityIterations);
    diffuse(diffuseVelocityYJob, viscosity, velocityIterations);

    project(velocityPreviousX, velocityPreviousY, velocityX, velocityY, velocityIterations);

    Threadanator.getInstance().processJob(advectVelocityXJob);

    Threadanator.getInstance().processJob(advectVelocityYJob);

    project(velocityX, velocityY, velocityPreviousX, velocityPreviousY, velocityIterations);
  }

  void project(float[] destinationVelocityX, float[] destinationVelocityY, float[] sourceVelocityX, float[] sourceVelocityY, int iterations)
  {
    float h = 1.0f / width;
    float halfHNegative = -0.5f * h;
    float halfNNegative = -0.5f * width;

    Threadanator threadanator = Threadanator.getInstance().prepare();

    for (int y = 1; y <= height; y++)
    {
      threadanator.add(new FluidProject1(this, destinationVelocityX, destinationVelocityY, sourceVelocityY, halfHNegative, y));
    }
    threadanator.process(16);

    Arrays.fill(sourceVelocityX, 0);

    for (int i = 0; i < iterations; i++)
    {
      threadanator.prepare();
      for (int y = 1; y <= height; y++)
      {
        threadanator.add(new FluidProject2(this, sourceVelocityX, sourceVelocityY, y));
      }
      threadanator.process(16);
    }

    threadanator.prepare();
    for (int y = 1; y <= height; y++)
    {
      threadanator.add(new FluidProject3(this, destinationVelocityX, destinationVelocityY, sourceVelocityX, halfNNegative, y));
    }
    threadanator.process(16);
  }

  public float sumAdjacentValues(int index, float[] values)
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

  private void addTimeScaled(float[] destination, float[] source, int stride, float timeStep)
  {
    Threadanator threadanator = Threadanator.getInstance().prepare();
    for (int y = 0; y < height + 2; y++)
    {
      int index = y * stride;
      threadanator.add(new TimeScaledWork(this, destination, source, timeStep, index));
    }

    threadanator.process(16);
  }


  public void tick()
  {
    long startTime = System.nanoTime();

    calculateVelocity();
    calculateDensity();

    long endTime = System.nanoTime();
    double timeInSeconds = (double) (endTime - startTime) / 1000000000;
    System.out.println(String.format("%.3f", timeInSeconds));
  }

  public int getStride()
  {
    return stride;
  }
}

