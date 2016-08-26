package net.kingdom.fluid;

import net.engine.thread.Job;
import net.engine.thread.Threadanator;
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
    diffuseVelocityYJob = createDiffuseJob(velocityPreviousY, velocityPreviousY, diffusionRate);

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

  void diffuse(Job diffuseJob,
               int boundaryHack,
               float[] destination,
               float[] source,
               float diffusionRate,
               int iterations)
  {
    if (diffusionRate != 0)
    {
      for (int i = 0; i < iterations; i++)
      {
        Threadanator.getInstance().processJob(diffuseJob);

        setBnd(width, height, boundaryHack, destination);
      }
    }
    else
    {
      copy(boundaryHack, destination, source);
    }
  }

  private Job createDiffuseJob(float[] destination, float[] source, float diffusionRate)
  {
    float a = timeStep * diffusionRate * width * height;
    float constant = 1.0f / (1.0f + (4 * a));

    Job diffuseJob = new Job(16);
    for (int y = 1; y <= height; y++)
    {
      int index = IX(1, y);
      diffuseJob.add(new FluidDiffuseWork(this, destination, source, a, constant, index));
    }
    return diffuseJob;
  }

  private void copy(int boundaryHack,
                    float[] destination,
                    float[] source)
  {
    System.arraycopy(source, 0, destination, 0, (stride) * (height + 2));

    setBnd(width, height, boundaryHack, destination);
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

    diffuse(diffuseDensityJob, 0, densityPrevious, density, diffusionRate, densityIterations);

    Threadanator.getInstance().processJob(advectDensityJob);

    setBnd(width, height, 0, density);
  }

  /**
   * 1 step of the velocity solver.
   */
  void calculateVelocity()
  {
    addTimeScaled(velocityX, velocityPreviousX, stride, timeStep);
    addTimeScaled(velocityY, velocityPreviousY, stride, timeStep);

    diffuse(diffuseVelocityXJob, 1, velocityPreviousX, velocityX, viscosity, velocityIterations);
    diffuse(diffuseVelocityYJob, 2, velocityPreviousY, velocityY, viscosity, velocityIterations);

    project(new FluidProjectParams(velocityPreviousX, velocityPreviousY, velocityX, velocityY), velocityIterations);

    Threadanator.getInstance().processJob(advectVelocityXJob);

    setBnd(width, height, 1, velocityX);
    Threadanator.getInstance().processJob(advectVelocityYJob);

    setBnd(width, height, 2, velocityY);

    project(new FluidProjectParams(velocityX, velocityY, velocityPreviousX, velocityPreviousY), velocityIterations);
  }

  void project(FluidProjectParams params, int iterations)
  {
    float h = 1.0f / width;
    float halfHNegative = -0.5f * h;
    float halfNNegative = -0.5f * width;

    Threadanator threadanator = Threadanator.getInstance().prepare();

    for (int y = 1; y <= height; y++)
    {
      int index = IX(1, y);
      threadanator.add(new FluidProject1(this, params, halfHNegative, index));
    }
    threadanator.process(16);

    Arrays.fill(params.sourceVelocityX, 0);

    setBnd(width, height, 0, params.sourceVelocityY);
    setBnd(width, height, 0, params.sourceVelocityX);

    for (int i = 0; i < iterations; i++)
    {
      threadanator.prepare();
      for (int y = 1; y <= height; y++)
      {
        int index = IX(1, y);
        threadanator.add(new FluidProject2(this, params, index));
      }
      threadanator.process(16);

      setBnd(width, height, 0, params.sourceVelocityX);
    }

    threadanator.prepare();
    for (int y = 1; y <= height; y++)
    {
      int index = IX(1, y);
      threadanator.add(new FluidProject3(this, params, halfNNegative, index));
    }
    threadanator.process(16);

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

