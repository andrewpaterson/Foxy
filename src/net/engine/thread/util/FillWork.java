package net.engine.thread.util;

import net.engine.thread.Work;

public class FillWork extends Work
{
  private float[] destination;
  private final int destinationOffset;
  private float value;
  private final int size;

  public FillWork(float[] destination, int destinationOffset, int size, float value)
  {
    this.destination = destination;
    this.destinationOffset = destinationOffset;
    this.value = value;
    this.size = size;
  }

  @Override
  public void work()
  {
    int end = destinationOffset + size;
    for (int i = destinationOffset; i < end; i++)
    {
      destination[i] = value;
    }
  }
}

