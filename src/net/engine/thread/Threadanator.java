package net.engine.thread;

import net.engine.thread.exception.AlreadyPreparedException;
import net.engine.thread.exception.NotPreparedException;

import java.util.ArrayList;
import java.util.List;

public class Threadanator
{
  private WorkQueue queue;
  private List<ThreadRunnable> threads;
  private Worker worker;
  private boolean prepared;

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
    this.worker = new Worker(queue);
    this.prepared = false;
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

    join();
  }

  private void work(int takeSize)
  {

    for (; ; )
    {
      if (!worker.work(takeSize))
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
    if (!prepared)
    {
      throw new NotPreparedException();
    }
    queue.add(work);
  }

  public void add(Job job)
  {
    if (!prepared)
    {
      throw new NotPreparedException();
    }

    List<Work> workList = job.getWorkList();
    for (Work work : workList)
    {
      queue.add(work);
    }
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

  private void join()
  {
    while (!areAllSleeping())
    {
    }

    if (prepared)
    {
      throw new AlreadyPreparedException();
    }
    prepared = true;
  }

  public void process(int takeSize)
  {
    multiThreaded(takeSize);
  }

  public void process(Job job)
  {
    add(job);
    process(!job.isMultiThreaded(), job.getTakeSize());
  }

  private void multiThreaded(int takeSize)
  {
    if (!prepared)
    {
      throw new NotPreparedException();
    }
    prepared = false;

    for (ThreadRunnable thread : threads)
    {
      thread.setTakeSize(takeSize);
      thread.interrupt();
    }

    work(takeSize);

    while (!queue.isEmpty())
    {
    }
    queue.clear();

    join();
  }

  private void singleThreaded()
  {
    if (!prepared)
    {
      throw new NotPreparedException();
    }
    prepared = false;

    work(1);
    queue.clear();
  }

  public void process(boolean singleThreaded, int takeSize)
  {
    if (singleThreaded)
    {
      singleThreaded();
    }
    else
    {
      multiThreaded(takeSize);
    }
  }

  public int getThreadCount()
  {
    return threads.size() + 1;
  }
}

