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
  float visc;
  float diff;

  public FluidField(int n, float dt, float visc, float diff)
  {
    // Number of columns and rows in our system
    this.N = n;
    this.dt = dt;
    this.visc = visc;
    this.diff = diff;

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
  int IX(int i, int j)
  {
    return (i + ((N + 2) * j));
  }

  /**
   * Sets boundary for diffusion. It is bound vertically and horizontally in a box.
   **/
  void setBnd(int n, int b, float[] x)
  {
    int i;

    for (i = 0; i <= n; i++)
    {
      x[IX(0, i)] = (b == 1 ? -x[IX(1, i)] : x[IX(1, i)]);
      x[IX(n + 1, i)] = (b == 1 ? -x[IX(n, i)] : x[IX(n, i)]);
      x[IX(i, 0)] = (b == 2 ? -x[IX(i, 1)] : x[IX(i, 1)]);
      x[IX(i, n + 1)] = (b == 2 ? -x[IX(i, n)] : x[IX(i, n)]);
    }
    x[IX(0, 0)] = 0.5f * (x[IX(1, 0)] + x[IX(0, 1)]);
    x[IX(0, n + 1)] = 0.5f * (x[IX(1, n + 1)] + x[IX(0, n)]);
    x[IX(n + 1, 0)] = 0.5f * (x[IX(n, 0)] + x[IX(n + 1, 1)]);
    x[IX(n + 1, n + 1)] = 0.5f * (x[IX(n, n + 1)] + x[IX(n + 1, n)]);
  }

  /**
   * 1st step:
   * Add a source held in s to the density held in x.
   * dt is the timestep and n+2 is the row and col size.
   */
  void addSource(int n, float[] x, float[] s, float dt)
  {
    int i;
    int sz = (n + 2) * (n + 2);

    for (i = 0; i < sz; i++)
    {
      x[i] += dt * s[i];
    }
  }

  /**
   * 2nd Step:
   * Diffusion at rate diff. Each cell will exchange density with direct neighbours.
   * Uses Gauss-Seidel relaxation.
   */
  void diffuse(int n, int b, float[] x, float[] x0, float diff, float dt)
  {
    int i, j, k;
    float a = dt * diff * n * n;

    for (k = 0; k < 20; k++)
    {
      for (i = 1; i <= n; i++)
      {
        for (j = 1; j <= n; j++)
        {
          x[IX(i, j)] = (x0[IX(i, j)] +
                  a * (x[IX(i - 1, j)] + x[IX(i + 1, j)] + x[IX(i, j - 1)] + x[IX(i, j + 1)])) / (1 + (4 * a));

        }
      }
      setBnd(n, b, x);
    }
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
        i0 = (int) x;
        i1 = i0 + 1;

        if (y < 0.5f)
        {
          y = 0.5f;
        }
        if (y > (n + 0.5f))
        {
          y = n + 0.5f;
        }
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
  void densStep(int n, float[] x, float[] x0, float[] u, float[] v, float diff, float dt)
  {
    addSource(n, x, x0, dt);

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
    addSource(n, u, u0, dt);
    addSource(n, v, v0, dt);

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
               float[] u,
               float[] v,
               float[] p,
               float[] div)
  {
    int i, j, k;
    float h = 1.0f / n;
    for (i = 1; i <= n; i++)
    {
      for (j = 1; j <= n; j++)
      {
        div[IX(i, j)] = -0.5f * h * (u[IX(i + 1, j)] - u[IX(i - 1, j)] +
                v[IX(i, j + 1)] - v[IX(i, j - 1)]);
        p[IX(i, j)] = 0;
      }
    }
    setBnd(n, 0, div);
    setBnd(n, 0, p);

    for (k = 0; k < 20; k++)
    {
      for (i = 1; i <= n; i++)
      {
        for (j = 1; j <= n; j++)
        {
          p[IX(i, j)] = (div[IX(i, j)] + p[IX(i - 1, j)] + p[IX(i + 1, j)] +
                  p[IX(i, j - 1)] + p[IX(i, j + 1)]) / 4;
        }
      }
      setBnd(n, 0, p);
    }

    for (i = 1; i <= n; i++)
    {
      for (j = 1; j <= n; j++)
      {
        u[IX(i, j)] -= 0.5f * (p[IX(i + 1, j)] - p[IX(i - 1, j)]) / h;
        v[IX(i, j)] -= 0.5f * (p[IX(i, j + 1)] - p[IX(i, j - 1)]) / h;
      }
    }
    setBnd(n, 1, u);
    setBnd(n, 2, v);
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
    velStep(N, u, v, u_prev, v_prev, visc, dt);
    densStep(N, dens, dens_prev, u, v, diff, dt);
  }
}

