package net.kingdom;

public class FluidField
{
  // Number of columns and rows in our system
  int N;

  int SIZE;

  //v is velocities
  float[] u;
  float[] v;
  float[] u_prev;
  float[] v_prev;

  float[] dens;
  float[] dens_prev;

  float dt;
  float viscosity;
  float attrition;

  public FluidField(int n, float dt, float viscosity, float attrition)
  {
    // Number of columns and rows in our system
    this.N = n;
    this.dt = dt;
    this.viscosity = viscosity;
    this.attrition = attrition;

    SIZE = (N + 2) * (N + 2);

    //v is velocities
    u = new float[SIZE];
    v = new float[SIZE];
    u_prev = new float[SIZE];
    v_prev = new float[SIZE];

    dens = new float[SIZE];
    dens_prev = new float[SIZE];
  }

  void clearData()
  {
    int i;
    int sz = (N + 2) * (N + 2);

    for (i = 0; i < sz; i++)
    {
      u[i] = 0.0f;
      v[i] = 0.0f;
      u_prev[i] = 0.0f;
      v_prev[i] = 0.0f;
      dens[i] = 0.0f;
      dens_prev[i] = 0.0f;
    }
  }

  //Helper function to get an element from a 1D array as if it were a 2D array
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
  void diffuse(int n, int boundaryHack, float[] destination, float[] source, float diff, float timeStep)
  {
    if (diff != 0)
    {
      float a = timeStep * diff * n * n;
      diffuse(n, boundaryHack, destination, source, a);
    }
    else
    {
      copy(n, boundaryHack, destination, source);
    }
  }

  private void diffuse(int n, int boundaryHack, float[] destination, float[] source, float a)
  {
    int iteration;
    int x;
    int y;
    for (iteration = 0; iteration < 1; iteration++)
    {
      for (x = 1; x <= n; x++)
      {
        for (y = 1; y <= n; y++)
        {
          int index = IX(x, y);
          float summedAdjacentValues = destination[index - 1] + destination[index + 1] + destination[index - n - 2] + destination[index + n + 2];
          destination[index] = (source[index] + a * summedAdjacentValues) / (1 + (4 * a));
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
              int b,
              float[] d,
              float[] d0,
              float[] u,
              float[] v,
              float dt)
  {
    int i, j, i0, j0, i1, j1;
    float x, y, s0, t0, s1, t1, dt0;

    dt0 = dt * n;
    for (i = 1; i <= n; i++)
    {
      for (j = 1; j <= n; j++)
      {
        x = i - dt0 * u[IX(i, j)];
        y = j - dt0 * v[IX(i, j)];

        if (x < 0.5f)
        {
          x = 0.5f;
        }
        if (x > (n + 0.5f))
        {
          x = n + 0.5f;
        }
        if (y < 0.5f)
        {
          y = 0.5f;
        }
        if (y > (n + 0.5f))
        {
          y = n + 0.5f;
        }

        i0 = (int) x;
        i1 = i0 + 1;

        j0 = (int) y;
        j1 = j0 + 1;

        s1 = x - i0;
        s0 = 1 - s1;

        t1 = y - j0;
        t0 = 1 - t1;

        d[IX(i, j)] = s0 * (t0 * d0[IX(i0, j0)] + t1 * d0[IX(i0, j1)]) +
                s1 * (t0 * d0[IX(i1, j0)] + t1 * d0[IX(i1, j1)]);
      }
    }
    setBnd(n, b, d);
  }

  /**
   * 1 step of the density solver.
   */
  void densStep(int n,
                float[] x,
                float[] x0,
                float[] u,
                float[] v,
                float diff,
                float dt)
  {
    addTimeScaled(n, n, x, x0, dt);

    //   swap(x0, x);
    float[] temp = x0;
    x0 = x;
    x = temp;

    diffuse(n, 0, x, x0, diff, dt);

    //   swap(x0, x);
    temp = x0;
    x0 = x;
    x = temp;

    advect(n, 0, x, x0, u, v, dt);
  }

  /**
   * 1 step of the velocity solver.
   */
  void velStep(int n,
               float[] u,
               float[] v,
               float[] u0,
               float[] v0,
               float visc,
               float dt)
  {
    addTimeScaled(n, n, u, u0, dt);
    addTimeScaled(n, n, v, v0, dt);

    //    swap(u0, u);
    float[] temp = u0;
    u0 = u;
    u = temp;

    diffuse(n, 1, u, u0, visc, dt);

    //    swap(v0, v);
    temp = v0;
    v0 = v;
    v = temp;

    diffuse(n, 2, v, v0, visc, dt);
    project(n, u, v, u0, v0);

    //    swap(u0, u);
    temp = u0;
    u0 = u;
    u = temp;

    //    swap(v0, v);
    temp = v0;
    v0 = v;
    v = temp;

    advect(n, 1, u, u0, u0, v0, dt);
    advect(n, 2, v, v0, u0, v0, dt);
    project(n, u, v, u0, v0);
  }

  void project(int n,
               float[] destinationVelocityX,
               float[] destinationVelocityY,
               float[] p,
               float[] div)
  {
    float h = 1.0f / n;
    for (int x = 1; x <= n; x++)
    {
      for (int y = 1; y <= n; y++)
      {
        div[IX(x, y)] = -0.5f * h * (destinationVelocityX[IX(x + 1, y)] - destinationVelocityX[IX(x - 1, y)] + destinationVelocityY[IX(x, y + 1)] - destinationVelocityY[IX(x, y - 1)]);
        p[IX(x, y)] = 0;
      }
    }
    setBnd(n, 0, div);
    setBnd(n, 0, p);

    for (int iterations = 0; iterations < 10; iterations++)
    {
      for (int x = 1; x <= n; x++)
      {
        for (int y = 1; y <= n; y++)
        {
          int index = IX(x, y);
          p[index] = (div[index] + p[index - 1] + p[index + 1] + p[index - n - 2] + p[index + n + 2]) / 4;
        }
      }
      setBnd(n, 0, p);
    }

    for (int x = 1; x <= n; x++)
    {
      for (int y = 1; y <= n; y++)
      {
        int index = IX(x, y);
        destinationVelocityX[index] -= 0.5f * (p[index + 1] - p[index - 1]) / h;
        destinationVelocityY[index] -= 0.5f * (p[index + n + 2] - p[index - n - 2]) / h;
      }
    }
    setBnd(n, 1, destinationVelocityX);
    setBnd(n, 2, destinationVelocityY);
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
    return dens[IX(x, y)];
  }

  public void setForce(int i, int j, float u, float v)
  {
    this.u[IX(i, j)] = u;
    this.v[IX(i, j)] = v;

  }

  public void setDensity(int i, int j, float source)
  {
    dens_prev[IX(i, j)] = source; //Set density to initial value
  }

  public void clearPrevious()
  {
    int size = SIZE;
    for (int i = 0; i < size; i++)
    {
      u_prev[i] = 0.0f;
      v_prev[i] = 0.0f;
      dens_prev[i] = 0.0f;
    }
  }

  public void tick()
  {
    long startTime = System.nanoTime();

    velStep(N, u, v, u_prev, v_prev, viscosity, dt);
    densStep(N, dens, dens_prev, u, v, attrition, dt);

    long endTime = System.nanoTime();
    double timeInSeconds = (double) (endTime - startTime) / 1000000000;
    System.out.println(String.format("%.3f", timeInSeconds));
  }
}

