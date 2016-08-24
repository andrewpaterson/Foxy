package net.engine.thread;

public class ThreadRunnable implements Runnable
{
  private WorkQueue queue;
  private Join join;
  private Thread thread;
  private boolean running;
  private boolean sleeping;

  public ThreadRunnable(WorkQueue queue, Join join)
  {
    this.queue = queue;
    this.join = join;
    this.running = true;
    sleeping = false;
  }

  @Override
  public void run()
  {
    while (running)
    {
      Work work = queue.take();
      if (work == null)
      {
        sleeping = true;
        boolean allDone = join.done(thread);
        if (!allDone)
        {
          try
          {
            thread.join();
          }
          catch (InterruptedException ignored)
          {
          }
        }
        else
        {
          join.interruptMainThread();
          try
          {
            thread.join();
          }
          catch (InterruptedException ignored)
          {
          }
        }

        while (sleeping)
        {
        }
      }
      else
      {
        work.work();
      }
    }
  }

  public void interrupt()
  {
    thread.interrupt();
  }

  public void setThread(Thread thread)
  {
    this.thread = thread;
  }

  public void start()
  {
    thread.start();
  }

  public boolean isRunning()
  {
    return running;
  }

  public boolean isSleeping()
  {
    return sleeping;
  }
  public void stopSleeping()
  {
    sleeping = false;
  }
}

