package net.engine.thread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 2016/08/23.
 */
public class JoinWork extends Work
{
  private int expectedThreads;
  private Thread joinThread;
  private List<Thread> threads;

  public JoinWork(int expectedThreads, Thread joinThread)
  {
    this.expectedThreads = expectedThreads;
    this.joinThread = joinThread;
    this.threads = new ArrayList<>();
  }

  @Override
  public void work()
  {
    Thread thread = addThread();
    while (threads.size() < expectedThreads)
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

  private synchronized Thread addThread()
  {
    Thread thread = Thread.currentThread();
    if (threads.size() == expectedThreads - 1)
    {
      joinThread.interrupt();
      for (Thread otherThread : threads)
      {
        otherThread.interrupt();
      }
    }
    threads.add(thread);
    return thread;
  }
}

