package net.engine.thread;

import java.util.concurrent.BlockingQueue;

/**
 * Created by andrew on 2016/08/23.
 */
public class ThreadRunnable implements Runnable
{
  private BlockingQueue<Work> queue;

  public ThreadRunnable(BlockingQueue<Work> queue)
  {
    this.queue = queue;
  }

  @Override
  public void run()
  {
    try
    {
      for (; ; )
      {
        Work work = queue.take();
        work.work();
      }
    }
    catch (InterruptedException ignored)
    {
    }
  }
}

