package net.engine.common;

public class Timer
{
  private long startTime;
  private long endTime;

  public Timer()
  {
    start();
  }

  public void start()
  {
    startTime = System.nanoTime();
  }

  public double stop()
  {
    endTime = System.nanoTime();
    return getTimeInSeconds();
  }

  public double getTimeInSeconds()
  {
    return (double) (endTime - startTime) / 1000000000;
  }
}
