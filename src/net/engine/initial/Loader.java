package net.engine.initial;

/**
 * Created by andrew on 2016/08/09.
 */
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

