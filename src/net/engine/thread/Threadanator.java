package net.engine.thread;

import java.util.ArrayList;
import java.util.List;

public class Threadanator
{
  private WorkQueue queue;
  private List<ThreadRunnable> threads;

  private static Threadanator instance = null;

  public static Threadanator getInstance()
  {
    if (instance == null)
    {
      instance = new Threadanator();
    }
    return instance;
  }

  public Threadanator()
  {
    int availableProcessors = Runtime.getRuntime().availableProcessors() - 1;
    this.queue = new WorkQueue();
    this.threads = new ArrayList<>(availableProcessors);
    for (int i = 0; i < availableProcessors; i++)
    {
      ThreadRunnable threadRunnable = new ThreadRunnable(queue);
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

    for (; ; )
    {
      if (areAllRunning())
      {
        break;
      }
    }

    for (; ; )
    {
      if (areAllSleeping())
      {
        break;
      }
    }
  }

  private void work()
  {
    for (; ; )
    {
      Work work = queue.take();
      if (work != null)
      {
        work.work();
      }
      else
      {
        break;
      }
    }
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

  public void add(Work work)
  {
    queue.add(work);
  }

  public void stop()
  {
    for (ThreadRunnable thread : threads)
    {
      thread.stopRunning();
    }
    for (ThreadRunnable thread : threads)
    {
      thread.interrupt();
    }
  }

  public void process()
  {
    processMultiThreaded();
  }

  private void processMultiThreaded()
  {
    while (!areAllSleeping())
    {
    }

    for (ThreadRunnable thread : threads)
    {
      thread.interrupt();
    }

    work();

    while (!queue.isEmpty())
    {
    }

    queue.clear();
  }

  public void process(boolean singleThreaded)
  {
    if (singleThreaded)
    {
      processSingleThreaded();
    }
    else
    {
      processMultiThreaded();
    }
  }

  private void processSingleThreaded()
  {
    work();
    queue.clear();
  }
}

