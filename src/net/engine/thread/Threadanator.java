package net.engine.thread;

import java.util.ArrayList;
import java.util.List;

public class Threadanator
{
  private final Thread mainThread;
  private WorkQueue queue;
  private List<ThreadRunnable> threads;
  private Join join;

  private static Threadanator instance = null;

  public static Threadanator getInstance()
  {
    if (instance == null)
    {
      instance = new Threadanator(Thread.currentThread());
    }
    return instance;
  }

  public Threadanator(Thread mainThread)
  {
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    this.queue = new WorkQueue();
    this.mainThread = mainThread;
    this.join = new Join(availableProcessors, mainThread);
    this.threads = new ArrayList<>(availableProcessors);
    for (int i = 0; i < availableProcessors; i++)
    {
      ThreadRunnable threadRunnable = new ThreadRunnable(queue, join);
      Thread thread = new Thread(threadRunnable);
      thread.setName("Worker " + (i + 1));
      threadRunnable.setThread(thread);
      this.threads.add(threadRunnable);
    }
  }

  public void start()
  {
    for (ThreadRunnable thread : threads)
    {
      thread.start();
    }

    for (;;)
    {
      if (areAllRunning())
      {
        break;
      }
    }

    for (;;)
    {
      if (areAllSleeping())
      {
        break;
      }
    }
    System.out.println("All Sleeping");
  }

  private boolean areAllRunning()
  {
    boolean allStarted = true;
    for (ThreadRunnable threadRunnable : threads)
    {
      if (!threadRunnable.isRunning())
      {
        allStarted = false;
      }
    }
    return allStarted;
  }

  private boolean areAllSleeping()
  {
    boolean allSleeping = true;
    for (ThreadRunnable threadRunnable : threads)
    {
      if (!threadRunnable.isSleeping())
      {
        allSleeping = false;
      }
    }
    return allSleeping;
  }

  private boolean areAllAwake()
  {
    boolean allAwake = true;
    for (ThreadRunnable threadRunnable : threads)
    {
      if (threadRunnable.isSleeping())
      {
        allAwake = false;
      }
    }
    return allAwake;
  }

  public void add(Work work)
  {
    queue.add(work);
  }

  public void stop()
  {
    StopWork stopWork = new StopWork();
    for (int i = 0; i < threads.size(); i++)
    {
      queue.add(stopWork);
    }
  }

  public int size()
  {
    return queue.size();
  }

  public void process()
  {
    for (ThreadRunnable thread : threads)
    {
      thread.interrupt();
    }

    for (ThreadRunnable thread : threads)
    {
      thread.stopSleeping();
    }

    for (;;)
    {
      if (areAllAwake())
      {
        break;
      }
    }

    try
    {
      mainThread.join();
    }
    catch (InterruptedException ignored)
    {
      System.out.println("Interrupted");
    }
    queue.clear();
  }
}

