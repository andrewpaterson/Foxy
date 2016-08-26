package net.engine.thread.util;

import net.engine.thread.Work;

public class CopyWork extends Work
{
  private float[] destination;
  private final int destinationOffset;
  private float[] source;
  private final int sourceOffset;
  private final int size;

  public CopyWork(float[] destination, int destinationOffset, float[] source, int sourceOffset, int size)
  {
    this.destination = destination;
    this.destinationOffset = destinationOffset;
    this.source = source;
    this.sourceOffset = sourceOffset;
    this.size = size;
  }

  @Override
  public void work()
  {
    System.arraycopy(source, sourceOffset, destination, destinationOffset, size);
  }
}
