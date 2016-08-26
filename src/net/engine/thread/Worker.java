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
      for (int i = 0; i < taken; i++)
      {
        Work work = workTaken[i];
        workTaken[i] = null;
        
        work.work();
      }
      return true;
    }
    else
    {
      return false;
    }
  }
}

