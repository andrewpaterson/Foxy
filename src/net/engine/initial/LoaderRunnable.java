package net.engine.initial;

/**
 * Created by andrew on 2016/08/09.
 */
public class LoaderRunnable implements Runnable
{
  private boolean done;
  private Loader loader;

  public LoaderRunnable(Loader loader)
  {
    this.loader = loader;
    done = false;
  }

  @Override
  public void run()
  {
    loader.load();
    done = true;
  }

  public boolean isDone()
  {
    return done;
  }
}

