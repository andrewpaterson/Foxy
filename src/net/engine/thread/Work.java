package net.engine.thread;

public abstract class Work
{
  public static int SUCCESS = 0;
  public static int FAILURE = 1;

  protected static int STOP = -1;
  protected static int WAIT = -2;

  public abstract int work();
}

