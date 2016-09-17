package net.engine.thread;

public class Worker
{
  private WorkQueue queue;
  private Work[] workTaken;

  public Worker(WorkQueue queue)
  {
    this(queue, 32);
  }

  public Worker(WorkQueue queue, int maxWork)
  {
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

        if (work != null)
        {
          work.work();
        }
      }
      return true;
    }
    else
    {
      return false;
    }
  }
}

