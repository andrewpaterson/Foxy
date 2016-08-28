package net.engine.thread.util;

import net.engine.thread.Job;
import net.engine.thread.Threadanator;
import net.engine.thread.Work;

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
      Work work = new CopyFloatArrayWork(destination, source, start, size);
      job.add(work);
    }

    return job;
  }

  public static Job copy(int[] destination, int[] source)
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
      Work work = new CopyIntArrayWork(destination, source, start, size);
      job.add(work);
    }

    return job;
  }

  public static Job copy(byte[] destination, byte[] source)
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
      Work work = new CopyByteArrayWork(destination, source, start, size);
      job.add(work);
    }

    return job;
  }
}

