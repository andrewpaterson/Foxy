package net.kingdom;

public class FluidField
{
  // Number of columns and rows in our system
  int width;
  int height;

  float[] velocityX;
  float[] velocityY;
  float[] velocityPreviousX;
  float[] velocityPreviousY;

  float[] density;
  float[] densityPrevious;

  float[] zero;

  float timeStep;
  float viscosity;
  float diffusionRate;

  public FluidField(int width, int height, float timeStep, float viscosity, float diffusionRate)
  {
    this.width = width;
    this.height = height;
    this.timeStep = timeStep;
    this.viscosity = viscosity;
    this.diffusionRate = diffusionRate;

    int size = (width + 2) * (height + 2);

    velocityX = new float[size];
    velocityY = new float[size];
    velocityPreviousX = new float[size];
    velocityPreviousY = new float[size];

    density = new float[size];
    densityPrevious = new float[size];

    zero = new float[size];
  }

  void clearData()
  {
    int i;
    int sz = (width + 2) * (height + 2);

    for (i = 0; i < sz; i++)
    {
      velocityX[i] = 0.0f;
      velocityY[i] = 0.0f;
      velocityPreviousX[i] = 0.0f;
      velocityPreviousY[i] = 0.0f;
      density[i] = 0.0f;
      densityPrevious[i] = 0.0f;
    }
  }

  int IX(int x, int y)
  {
    return (x + ((width + 2) * y));
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

  void addTimeScaled(int width, int height, float[] destination, float[] source, float timeStep)
  {
    int i;
    int size = (width + 2) * (height + 2);

    for (i = 0; i < size; i++)
    {
      destination[i] += timeStep * source[i];
    }
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
      diffuse(width, height, boundaryHack, destination, source, a, iterations);
    }
    else
    {
      copy(width, height, boundaryHack, destination, source);
    }
  }

  private void diffuse(int width,
                       int height,
                       int boundaryHack,
                       float[] destination,
                       float[] source,
                       float a,
                       int iterations)
  {
    float constant = 1.0f / (1.0f + (4 * a));
    for (int i = 0; i < iterations; i++)
    {
      for (int y = 1; y <= height; y++)
      {
        for (int x = 1; x <= width; x++)
        {
          int index = IX(x, y);
          float adjacentValueSum = sumAdjacentValues(index, destination, width);
          destination[index] = constant * (source[index] + a * adjacentValueSum);
        }
      }
      setBnd(width, height, boundaryHack, destination);
    }
  }

  private void copy(int width,
                    int height,
                    int boundaryHack,
                    float[] destination,
                    float[] source)
  {
    System.arraycopy(source, 0, destination, 0, (width + 2) * (height + 2));
    setBnd(width, height, boundaryHack, destination);
  }

  void advect(int width,
              int height,
              int boundaryHack,
              float[] density,
              float[] densityPrevious,
              float[] velocityX,
              float[] velocityY,
              float dt)
  {
    int xIndex, yIndex, xIndex1, yIndex1;
    float newX, newY, oneMinusXVelocityDecimal, oneMinusYVelocityDecimal, xVelocityDecimal, yVelocityDecimal;

    float timeStepScaledByWidth = dt * width;
    float timeStepScaledByHeight = dt * height;
    for (int y = 1; y <= height; y++)
    {
      for (int x = 1; x <= width; x++)
      {
        int index = IX(x, y);

        newX = x - timeStepScaledByWidth * velocityX[index];
        newY = y - timeStepScaledByHeight * velocityY[index];

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

        density[index] = oneMinusXVelocityDecimal * (oneMinusYVelocityDecimal * densityPrevious[IX(xIndex, yIndex)] + yVelocityDecimal * densityPrevious[IX(xIndex, yIndex1)]) +
                xVelocityDecimal * (oneMinusYVelocityDecimal * densityPrevious[IX(xIndex1, yIndex)] + yVelocityDecimal * densityPrevious[IX(xIndex1, yIndex1)]);
      }
    }

    setBnd(width, height, boundaryHack, density);
  }

  void calculateDensity(int width,
                        int height,
                        float[] density,
                        float[] densityPrevious,
                        float[] velocityX,
                        float[] velocityY,
                        float diffusionRate,
                        float timeStep,
                        int iterations)
  {
    addTimeScaled(width, height, density, densityPrevious, timeStep);

    diffuse(width, height, 0, densityPrevious, density, diffusionRate, timeStep, iterations);

    advect(width, height, 0, density, densityPrevious, velocityX, velocityY, timeStep);
  }

  /**
   * 1 step of the velocity solver.
   */
  void calculateVelocity(int width,
                         int height,
                         float[] velocityX,
                         float[] velocityY,
                         float[] velocityPreviousX,
                         float[] velocityPreviousY,
                         float viscosity,
                         float timeStep,
                         int iterations)
  {
    addTimeScaled(width, height, velocityX, velocityPreviousX, timeStep);
    addTimeScaled(width, height, velocityY, velocityPreviousY, timeStep);

    diffuse(width, height, 1, velocityPreviousX, velocityX, viscosity, timeStep, 10);
    diffuse(width, height, 2, velocityPreviousY, velocityY, viscosity, timeStep, 10);

    project(width, height, velocityPreviousX, velocityPreviousY, velocityX, velocityY, iterations);

    advect(width, height, 1, velocityX, velocityPreviousX, velocityPreviousX, velocityPreviousY, timeStep);
    advect(width, height, 2, velocityY, velocityPreviousY, velocityPreviousX, velocityPreviousY, timeStep);

    project(width, height, velocityX, velocityY, velocityPreviousX, velocityPreviousY, iterations);
  }

  void project(int width,
               int height,
               float[] destinationVelocityX,
               float[] destinationVelocityY,
               float[] p,
               float[] div,
               int iterations)
  {
    float h = 1.0f / width;
    float halfHNegative = -0.5f * h;
    float halfNNegative = -0.5f * width;

    for (int y = 1; y <= height; y++)
    {
      for (int x = 1; x <= width; x++)
      {
        int index = IX(x, y);
        div[index] = halfHNegative * (destinationVelocityX[index + 1] - destinationVelocityX[index - 1] + destinationVelocityY[index + width + 2] - destinationVelocityY[index - width - 2]);
      }
    }

    System.arraycopy(zero, 0, p, 0, (width + 2) * (height + 2));

    setBnd(width, height, 0, div);
    setBnd(width, height, 0, p);

    float scale = 1.0f / 4.0f;
    for (int i = 0; i < iterations; i++)
    {
      for (int y = 1; y <= height; y++)
      {
        for (int x = 1; x <= width; x++)
        {
          int index = IX(x, y);
          p[index] = scale * (div[index] + sumAdjacentValues(index, p, width));
        }
      }

      setBnd(width, height, 0, p);
    }

    for (int y = 1; y <= height; y++)
    {
      for (int x = 1; x <= width; x++)
      {
        int index = IX(x, y);
        destinationVelocityX[index] += halfNNegative * (p[index + 1] - p[index - 1]);
        destinationVelocityY[index] += halfNNegative * (p[index + width + 2] - p[index - width - 2]);
      }
    }
    setBnd(width, height, 1, destinationVelocityX);
    setBnd(width, height, 2, destinationVelocityY);
  }

  private float sumAdjacentValues(int index, float[] values, int width)
  {
    return values[index - 1] + values[index + 1] + values[index - width - 2] + values[index + width + 2];
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

  public void clearPrevious(int width, int height)
  {
    int size = (width + 2) * (height + 2);
    for (int i = 0; i < size; i++)
    {
      velocityPreviousX[i] = 0.0f;
      velocityPreviousY[i] = 0.0f;
      densityPrevious[i] = 0.0f;
    }
  }

  public void tick()
  {
    long startTime = System.nanoTime();

    calculateVelocity(width, height, velocityX, velocityY, velocityPreviousX, velocityPreviousY, viscosity, timeStep, 10);
    calculateDensity(width, height, density, densityPrevious, velocityX, velocityY, diffusionRate, timeStep, 10);

    long endTime = System.nanoTime();
    double timeInSeconds = (double) (endTime - startTime) / 1000000000;
    System.out.println(String.format("%.3f", timeInSeconds));
  }
}

