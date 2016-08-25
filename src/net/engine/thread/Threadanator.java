package net.engine.thread;

import java.util.ArrayList;
import java.util.List;

public class Threadanator
{
  private WorkQueue queue;
  private List<ThreadRunnable> threads;
  private JoinWork joinWork;
  private StopWork stopWork;

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
    this.joinWork = new JoinWork();
    this.stopWork = new StopWork();
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
        int result = work.work();
        if (result == Work.STOP || result == Work.WAIT)
        {
          break;
        }
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
    while (!areAllAwake())
    {
    }

    work();

    while (!areAllSleeping())
    {
      for (ThreadRunnable thread : threads)
      {
        if (!thread.isRunning())
        {
          threads.remove(thread);
          break;
        }
      }
    }

    queue.clear();
  }

  public void addWait()
  {
    for (int i = 0; i <= threads.size(); i++)
    {
      queue.add(joinWork);
    }
  }

  public void addStop()
  {
    for (int i = 0; i <= threads.size(); i++)
    {
      queue.add(stopWork);
    }
  }
}

