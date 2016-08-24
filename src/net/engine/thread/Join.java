package net.engine.thread;

import java.util.ArrayList;
import java.util.List;

public class Join
{
  private int expectedThreads;
  private Thread mainThread;
  private List<Thread> threads;

  public Join(int expectedThreads, Thread mainThread)
  {
    this.expectedThreads = expectedThreads;
    this.mainThread = mainThread;
    this.threads = new ArrayList<>();
  }

  public void interruptMainThread()
  {
    mainThread.interrupt();
  }

  public synchronized boolean done(Thread thread)
  {
    if (threads.size() == expectedThreads - 1)
    {
      threads.clear();
      return true;
    }
    else
    {
      threads.add(thread);
      return false;
    }
  }
}

