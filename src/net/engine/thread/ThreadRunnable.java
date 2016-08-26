package net.engine.thread;

public class ThreadRunnable implements Runnable
{
  private WorkQueue queue;
  private Thread thread;
  private boolean running;
  private boolean sleeping;

  public ThreadRunnable(WorkQueue queue)
  {
    this.queue = queue;
    this.running = true;
    this.sleeping = false;
  }

  @Override
  public void run()
  {
    sleep();

    while (running)
    {
      if (!sleeping)
      {
        Work work = queue.take();
        if (work != null)
        {
          work.work();
        }
        else
        {
          sleep();
        }
      }
    }
  }

  private void sleep()
  {
    sleeping = true;
    try
    {
      while (sleeping)
      {
        Thread.sleep(10);
      }
    }
    catch (InterruptedException e)
    {
      sleeping = false;
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

  public void stopRunning()
  {
    running = false;
  }
}

