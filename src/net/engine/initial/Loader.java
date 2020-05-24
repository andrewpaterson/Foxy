package net.engine.initial;

public abstract class Loader
{
  private LoaderRunnable runnable;

  public Loader()
  {
    this.runnable = new LoaderRunnable(this);
    Thread thread = new Thread(this.runnable);
    thread.start();
  }

  public boolean isDone()
  {
    return runnable.isDone();
  }

  public abstract void load();
}

