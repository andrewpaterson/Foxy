package net.engine.thread;

public class StopWork extends Work
{
  @Override
  public int work()
  {
    return STOP;
  }
}

