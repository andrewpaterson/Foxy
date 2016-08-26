package net.engine.thread;

public class Worker
{
  private WorkQueue queue;
  private Work[] workTaken;

  public Worker(WorkQueue queue)
  {
    this(queue, 16);
  }

  public Worker(WorkQueue queue, int maxWork)
  {
    if (maxWork < 16)
    {
      maxWork = 16;
    }
    this.queue = queue;
    this.workTaken = new Work[maxWork];
  }

  public boolean work(int takeSize)
  {
    int taken = queue.take(takeSize, workTaken);
    if (taken != 0)
    {
      int i;
      for (i = 0; i < taken; i++)
      {
        Work work = workTaken[i];
        work.work();
      }
      workTaken[0] = workTaken[1] = workTaken[2] = workTaken[3] = workTaken[4] = workTaken[5] = workTaken[6] = workTaken[7] = null;
      workTaken[8] = workTaken[9] = workTaken[10] = workTaken[11] = workTaken[12] = workTaken[13] = workTaken[14] = workTaken[15] = null;
      return true;
    }
    else
    {
      return false;
    }
  }
}

