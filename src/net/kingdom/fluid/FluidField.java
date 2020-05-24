package net.kingdom.fluid;

import net.engine.common.Timer;
import net.engine.thread.Job;
import net.engine.thread.Threadanator;
import net.engine.thread.util.CopyJob;
import net.engine.thread.util.FillJob;
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

  private Job timeScaleDensityJob;
  private Job timeScaleVelocityXJob;
  private Job timeScaleVelocityYJob;

  private Job projectJob1A;
  private Job projectJob2A;
  private Job projectJob3A;
  private Job projectJob1B;
  private Job projectJob2B;
  private Job projectJob3B;
  private Job projectJobFillA;
  private Job projectJobFillB;

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

    advectDensityJob = createAdvectJob(density, densityPrevious, velocityX, velocityY);
    advectVelocityXJob = createAdvectJob(velocityX, velocityPreviousX, velocityPreviousX, velocityPreviousY);
    advectVelocityYJob = createAdvectJob(velocityY, velocityPreviousY, velocityPreviousX, velocityPreviousY);

    diffuseDensityJob = createDiffuseJob(densityPrevious, density, diffusionRate);
    diffuseVelocityXJob = createDiffuseJob(velocityPreviousX, velocityX, viscosity);
    diffuseVelocityYJob = createDiffuseJob(velocityPreviousY, velocityY, viscosity);

    timeScaleDensityJob = createAddTimeScaledJob(density, densityPrevious);
    timeScaleVelocityXJob = createAddTimeScaledJob(velocityX, velocityPreviousX);
    timeScaleVelocityYJob = createAddTimeScaledJob(velocityY, velocityPreviousY);

    projectJob1A = createProjectJob1(velocityPreviousX, velocityPreviousY, velocityY);
    projectJobFillA = FillJob.fill(velocityX, 0);
    projectJob2A = createProjectJob2(velocityX, velocityY);
    projectJob3A = createProjectJob3(velocityPreviousX, velocityPreviousY, velocityX);
    projectJob1B = createProjectJob1(velocityX, velocityY, velocityPreviousY);
    projectJobFillB = FillJob.fill(velocityPreviousX, 0);
    projectJob2B = createProjectJob2(velocityPreviousX, velocityPreviousY);
    projectJob3B = createProjectJob3(velocityX, velocityY, velocityPreviousX);

    clearData();
  }

  public void clearData()
  {
    Arrays.fill(velocityX, 0.0f);
    Arrays.fill(velocityY, 0.0f);
    Arrays.fill(velocityPreviousX, 0.0f);
    Arrays.fill(velocityPreviousY, 0.0f);
    Arrays.fill(density, 0.01f);
    Arrays.fill(densityPrevious, 0.0f);
  }

  public int IX(int x, int y)
  {
    return x + stride * y;
  }

  private Job createDiffuseJob(float[] destination, float[] source, float diffusionRate)
  {
    if (diffusionRate != 0)
    {
      float a = timeStep * diffusionRate * width * height;
      float constant = 1.0f / (1.0f + (4 * a));

      Job diffuseJob = new Job(8);
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

  private Job createAdvectJob(float[] density, float[] densityPrevious, float[] velocityX, float[] velocityY)
  {
    Job job = new Job(8);
    float timeStepScaledByWidth = timeStep * width;
    float timeStepScaledByHeight = timeStep * height;

    for (int y = 1; y <= height; y++)
    {
      job.add(new FluidAdvectWork(this, density, densityPrevious, velocityX, velocityY, y, timeStepScaledByWidth, timeStepScaledByHeight));
    }

    return job;
  }

  private Job createProjectJob1(float[] destinationVelocityX, float[] destinationVelocityY, float[] sourceVelocityY)
  {
    Job projectJob1A = new Job(8);
    float h = 1.0f / width;
    float halfHNegative = -0.5f * h;
    for (int y = 1; y <= height; y++)
    {
      projectJob1A.add(new FluidProject1(this, destinationVelocityX, destinationVelocityY, sourceVelocityY, halfHNegative, y));
    }
    return projectJob1A;
  }

  private Job createProjectJob2(float[] sourceVelocityX, float[] sourceVelocityY)
  {
    Job projectJob2A = new Job(8);
    for (int y = 1; y <= height; y++)
    {
      projectJob2A.add(new FluidProject2(this, sourceVelocityX, sourceVelocityY, y));
    }
    return projectJob2A;
  }

  private Job createProjectJob3(float[] destinationVelocityX, float[] destinationVelocityY, float[] sourceVelocityX)
  {
    Job projectJob3A = new Job(8);
    float halfNNegative = -0.5f * width;
    for (int y = 1; y <= height; y++)
    {
      projectJob3A.add(new FluidProject3(this, destinationVelocityX, destinationVelocityY, sourceVelocityX, halfNNegative, y));
    }
    return projectJob3A;
  }

  private Job createAddTimeScaledJob(float[] destination, float[] source)
  {
    Job job = new Job(8);
    for (int y = 0; y < height + 2; y++)
    {
      job.add(new TimeScaledWork(this, destination, source, y));
    }

    return job;
  }

  void calculateDensity()
  {
    Threadanator.getInstance().process(timeScaleDensityJob);

    diffuse(diffuseDensityJob, diffusionRate, densityIterations);

    Threadanator.getInstance().process(advectDensityJob);
  }

  void calculateVelocity()
  {
    Threadanator.getInstance().process(timeScaleVelocityXJob);
    Threadanator.getInstance().process(timeScaleVelocityYJob);

    diffuse(diffuseVelocityXJob, viscosity, velocityIterations);
    diffuse(diffuseVelocityYJob, viscosity, velocityIterations);

    project(projectJob1A, projectJobFillA, projectJob2A, projectJob3A);

    Threadanator.getInstance().process(advectVelocityXJob);
    Threadanator.getInstance().process(advectVelocityYJob);

    project(projectJob1B, projectJobFillB, projectJob2B, projectJob3B);
  }

  void diffuse(Job diffuseJob,
               float rate,
               int iterations)
  {
    if (rate != 0)
    {
      for (int i = 0; i < iterations; i++)
      {
        Threadanator.getInstance().process(diffuseJob);
      }
    }
    else
    {
      Threadanator.getInstance().process(diffuseJob);
    }
  }

  void project(Job job1, Job jobFill, Job job2, Job job3)
  {
    Threadanator.getInstance().process(job1);

    Threadanator.getInstance().process(jobFill);

    for (int i = 0; i < velocityIterations; i++)
    {
      Threadanator.getInstance().process(job2);
    }

    Threadanator.getInstance().process(job3);
  }

  public float sumAdjacentValues(int index, float[] values)
  {
    return values[index - 1] + values[index + 1] + values[index - stride] + values[index + stride];
  }

  public float getDensity(int x, int y)
  {
    return density[IX(x, y)];
  }

  public float getVelocityX(int x, int y)
  {
    return velocityX[IX(x, y)];
  }

  public float getVelocityY(int x, int y)
  {
    return velocityY[IX(x, y)];
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

  public double tick()
  {
    Timer timer = new Timer();

    calculateVelocity();
    calculateDensity();

    return timer.stop();
  }

  public int getWidth()
  {
    return width;
  }

  public int getHeight()
  {
    return height;
  }

  public int getStride()
  {
    return stride;
  }

  public float getTimeStep()
  {
    return timeStep;
  }
}

