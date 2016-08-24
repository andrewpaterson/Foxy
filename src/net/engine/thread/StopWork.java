package net.engine.thread;

public class StopWork extends Work
{
  @Override
  public void work()
  {
    Thread thread = Thread.currentThread();
    thread.interrupt();
  }
}

