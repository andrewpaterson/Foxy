package net.engine.thread;

/**
 * Created by andrew on 2016/08/23.
 */
public class StopWork extends Work
{
  @Override
  public void work()
  {
    Thread thread = Thread.currentThread();
    thread.interrupt();
  }
}

