package net.engine.thread.util;

import net.engine.thread.Job;
import net.engine.thread.Threadanator;

public class CopyJob
{
  public static Job copy(float[] destination, float[] source)
  {
    int threadCount = Threadanator.getInstance().getThreadCount();
    int arraySize = Math.min(source.length, destination.length);
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
      CopyWork copyWork = new CopyWork(destination, source, start, size);
      job.add(copyWork);
    }

    return job;
  }
}

