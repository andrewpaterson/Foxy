package net.kingdom;

public class FluidField
{
  // Number of columns and rows in our system
  int N;

  int SIZE;

  //v is velocities
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

  public FluidField(int n, float timeStep, float viscosity, float diffusionRate)
  {
    // Number of columns and rows in our system
    this.N = n;
    this.timeStep = timeStep;
    this.viscosity = viscosity;
    this.diffusionRate = diffusionRate;

    SIZE = (N + 2) * (N + 2);

    //v is velocities
    velocityX = new float[SIZE];
    velocityY = new float[SIZE];
    velocityPreviousX = new float[SIZE];
    velocityPreviousY = new float[SIZE];

    density = new float[SIZE];
    densityPrevious = new float[SIZE];

    zero = new float[SIZE];
  }

  void clearData()
  {
    int i;
    int sz = (N + 2) * (N + 2);

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
    return (x + ((N + 2) * y));
  }

  /**
   * Sets boundary for diffusion. It is bound vertically and horizontally in a box.
   **/

  void setBnd(int n, int boundaryHack, float[] destination)
  {
    int i;

    if (boundaryHack == 0)
    {
      for (i = 0; i <= n; i++)
      {
        destination[IX(0, i)] = destination[IX(1, i)];
        destination[IX(n + 1, i)] = destination[IX(n, i)];
        destination[IX(i, 0)] = destination[IX(i, 1)];
        destination[IX(i, n + 1)] = destination[IX(i, n)];
      }
    }

    if (boundaryHack == 1)
    {
      for (i = 0; i <= n; i++)
      {
        destination[IX(0, i)] = -destination[IX(1, i)];
        destination[IX(n + 1, i)] = -destination[IX(n, i)];
        destination[IX(i, 0)] = destination[IX(i, 1)];
        destination[IX(i, n + 1)] = destination[IX(i, n)];
      }
    }

    if (boundaryHack == 2)
    {
      for (i = 0; i <= n; i++)
      {
        destination[IX(0, i)] = destination[IX(1, i)];
        destination[IX(n + 1, i)] = destination[IX(n, i)];
        destination[IX(i, 0)] = -destination[IX(i, 1)];
        destination[IX(i, n + 1)] = -destination[IX(i, n)];
      }
    }

    destination[IX(0, 0)] = 0.5f * (destination[IX(1, 0)] + destination[IX(0, 1)]);
    destination[IX(0, n + 1)] = 0.5f * (destination[IX(1, n + 1)] + destination[IX(0, n)]);
    destination[IX(n + 1, 0)] = 0.5f * (destination[IX(n, 0)] + destination[IX(n + 1, 1)]);
    destination[IX(n + 1, n + 1)] = 0.5f * (destination[IX(n, n + 1)] + destination[IX(n + 1, n)]);
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

  /**
   * 2nd Step:
   * Diffusion at rate diff. Each cell will exchange density with direct neighbours.
   * Uses Gauss-Seidel relaxation.
   */
  void diffuse(int n,
               int boundaryHack,
               float[] destination,
               float[] source,
               float diff,
               float timeStep,
               int iterations)
  {
    if (diff != 0)
    {
      float a = timeStep * diff * n * n;
      diffuse(n, boundaryHack, destination, source, a, iterations);
    }
    else
    {
      copy(n, boundaryHack, destination, source);
    }
  }

  private void diffuse(int n,
                       int boundaryHack,
                       float[] destination,
                       float[] source,
                       float a,
                       int iterations)
  {
    int i;
    int x;
    int y;

    float constant = 1.0f / (1.0f + (4 * a));
    for (i = 0; i < iterations; i++)
    {
      for (x = 1; x <= n; x++)
      {
        for (y = 1; y <= n; y++)
        {
          int index = IX(x, y);
          float adjacentValueSum = sumAdjacentValues(index, destination, n);
          destination[index] = constant * (source[index] + a * adjacentValueSum);
        }
      }
      setBnd(n, boundaryHack, destination);
    }
  }

  private void copy(int n, int boundaryHack, float[] destination, float[] source)
  {
    System.arraycopy(source, 0, destination, 0, SIZE);
    setBnd(n, boundaryHack, destination);
  }

  /**
   * 3rd Step :
   * Calculating advections. This ensures that the density follows a given velocity field.
   **/
  void advect(int n,
              int boundaryHack,
              float[] density,
              float[] densityPrevious,
              float[] velocityU,
              float[] velocityV,
              float dt)
  {
    int xIndex, yIndex, xIndex1, yIndex1;
    float newX, newY, oneMinusXVelocityDecimal, oneMinusYVelocityDecimal, xVelocityDecimal, yVelocityDecimal, timeStepScaledByWidth;

    timeStepScaledByWidth = dt * n;
    for (int x = 1; x <= n; x++)
    {
      for (int y = 1; y <= n; y++)
      {
        int index = IX(x, y);

        newX = x - timeStepScaledByWidth * velocityU[index];
        newY = y - timeStepScaledByWidth * velocityV[index];

        if (newX < 0.5f)
        {
          newX = 0.5f;
        }
        if (newX > (n + 0.5f))
        {
          newX = n + 0.5f;
        }
        if (newY < 0.5f)
        {
          newY = 0.5f;
        }
        if (newY > (n + 0.5f))
        {
          newY = n + 0.5f;
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

    setBnd(n, boundaryHack, density);
  }

  /**
   * 1 step of the density solver.
   */
  void densStep(int n,
                float[] density,
                float[] densityPrevious,
                float[] velocityX,
                float[] velocityY,
                float diff,
                float timeStep,
                int iterations)
  {
    addTimeScaled(n, n, density, densityPrevious, timeStep);

    //   swap(x0, x);
    float[] temp = densityPrevious;
    densityPrevious = density;
    density = temp;

    diffuse(n, 0, density, densityPrevious, diff, timeStep, iterations);

    //   swap(x0, x);
    temp = densityPrevious;
    densityPrevious = density;
    density = temp;

    advect(n, 0, density, densityPrevious, velocityX, velocityY, timeStep);
  }

  /**
   * 1 step of the velocity solver.
   */
  void velocityStep(int n,
                    float[] velocityX,
                    float[] velocityY,
                    float[] velocityPreviousX,
                    float[] velocityPreviousY,
                    float viscosity,
                    float timeStep,
                    int iterations)
  {
    addTimeScaled(n, n, velocityX, velocityPreviousX, timeStep);
    addTimeScaled(n, n, velocityY, velocityPreviousY, timeStep);

    diffuse(n, 1, velocityPreviousX, velocityX, viscosity, timeStep, 10);
    diffuse(n, 2, velocityPreviousY, velocityY, viscosity, timeStep, 10);

    project(n, velocityPreviousX, velocityPreviousY, velocityX, velocityY, iterations);

    advect(n, 1, velocityX, velocityPreviousX, velocityPreviousX, velocityPreviousY, timeStep);
    advect(n, 2, velocityY, velocityPreviousY, velocityPreviousX, velocityPreviousY, timeStep);

    project(n, velocityX, velocityY, velocityPreviousX, velocityPreviousY, iterations);
  }

  void project(int n,
               float[] destinationVelocityX,
               float[] destinationVelocityY,
               float[] p,
               float[] div,
               int iterations)
  {
    float h = 1.0f / n;
    float halfHNegative = -0.5f * h;
    float halfNNegative = -0.5f * n;

    for (int x = 1; x <= n; x++)
    {
      for (int y = 1; y <= n; y++)
      {
        int index = IX(x, y);
        div[index] = halfHNegative * (destinationVelocityX[index + 1] - destinationVelocityX[index - 1] + destinationVelocityY[index + n + 2] - destinationVelocityY[index - n - 2]);
      }
    }

    System.arraycopy(zero, 0, p, 0, SIZE);

    setBnd(n, 0, div);
    setBnd(n, 0, p);

    for (int i = 0; i < iterations; i++)
    {
      for (int x = 1; x <= n; x++)
      {
        for (int y = 1; y <= n; y++)
        {
          int index = IX(x, y);
          p[index] = 0.25f * (div[index] + sumAdjacentValues(index, p, n));
        }
      }

      setBnd(n, 0, p);
    }

    for (int x = 1; x <= n; x++)
    {
      for (int y = 1; y <= n; y++)
      {
        int index = IX(x, y);
        destinationVelocityX[index] += halfNNegative * (p[index + 1] - p[index - 1]);
        destinationVelocityY[index] += halfNNegative * (p[index + n + 2] - p[index - n - 2]);
      }
    }
    setBnd(n, 1, destinationVelocityX);
    setBnd(n, 2, destinationVelocityY);
  }

  private float sumAdjacentValues(int index, float[] values, int width)
  {
    return values[index - 1] + values[index + 1] + values[index - width - 2] + values[index + width + 2];
  }

  public int getWidth()
  {
    return N;
  }

  public int getHeight()
  {
    return N;
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
    densityPrevious[IX(i, j)] = source; //Set density to initial value
  }

  public void clearPrevious()
  {
    int size = SIZE;
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

    velocityStep(N, velocityX, velocityY, velocityPreviousX, velocityPreviousY, viscosity, timeStep, 10);
    densStep(N, density, densityPrevious, velocityX, velocityY, diffusionRate, timeStep, 10);

    long endTime = System.nanoTime();
    double timeInSeconds = (double) (endTime - startTime) / 1000000000;
    System.out.println(String.format("%.3f", timeInSeconds));
  }
}

