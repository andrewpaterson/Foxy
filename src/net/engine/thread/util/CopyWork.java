package net.engine.thread.util;

import net.engine.thread.Work;

public class CopyWork extends Work
{
  private float[] destination;
  private float[] source;
  private int offset;
  private int size;

  public CopyWork(float[] destination, float[] source, int offset, int size)
  {
    this.destination = destination;
    this.source = source;
    this.offset = offset;
    this.size = size;
  }

  @SuppressWarnings("ManualArrayCopy")
  @Override
  public void work()
  {
    int end = offset + size;
    for (int i = offset; i < end; i++)
    {
      destination[i] = source[i];
    }
  }
}

