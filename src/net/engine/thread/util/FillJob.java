package net.engine.thread.util;

import net.engine.thread.Job;
import net.engine.thread.Threadanator;

public class FillJob
{
  public static Job fill(float[] destination, float value)
  {
    int threadCount = Threadanator.getInstance().getThreadCount();
    int arraySize = destination.length;
    int copySize = arraySize / threadCount;
    int sixteenths = copySize / 16;
    int remainder = copySize % 16;

    if (remainder != 0)
    {
      copySize = (sixteenths + 1) * 16;
    }

    Job job = new Job(1);
    for (int i = 0; i < threadCount; i++)
    {
      int start = copySize * i;
      int size = copySize;
      if (size + start > arraySize)
      {
        size = arraySize - start;
      }
      FillWork fillWork = new FillWork(destination, start, size, value);
      job.add(fillWork);
    }

    return job;
  }
}
