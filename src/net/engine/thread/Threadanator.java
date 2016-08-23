package net.engine.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Threadanator
{
  private BlockingQueue<Work> queue;
  private List<Thread> threads;

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
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    queue = new LinkedBlockingQueue<>();
    threads = new ArrayList<>(availableProcessors);
    for (int i = 0; i < availableProcessors; i++)
    {
      Thread thread = new Thread(new ThreadRunnable(queue));
      threads.add(thread);
    }
  }

  public void start()
  {
    for (Thread thread : threads)
    {
      thread.start();
    }
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

  public void join()
  {
    Thread thread = Thread.currentThread();

    JoinWork joinWork = new JoinWork(threads.size(), thread);
    for (int i = 0; i < threads.size(); i++)
    {
      queue.add(joinWork);
    }
    while (!queue.isEmpty())
    {
      try
      {
        thread.join();
      }
      catch (InterruptedException e)
      {
        break;
      }
    }
  }
}

