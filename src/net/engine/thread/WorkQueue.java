package net.engine.thread;

import java.util.ArrayList;
import java.util.List;

public class WorkQueue
{
  private List<Work> work;
  private int index;
  private int length;

  public WorkQueue()
  {
    work = new ArrayList<>();
    index = -1;
  }

  public synchronized Work take()
  {
    if (index < length - 1)
    {
      index++;
      return work.get(index);
    }
    return null;
  }

  public void clear()
  {
    length = 0;
    index = -1;
    for (int i = 0; i < work.size(); i++)
    {
      work.set(i, null);
    }
  }

  public void add(Work work)
  {
    if (length < this.work.size())
    {
      this.work.set(length, work);
    }
    else
    {
      this.work.add(work);
    }
    length++;
  }

  public synchronized int size()
  {
    return length - (index + 1);
  }

  public synchronized boolean isEmpty()
  {
    return length == index;
  }
}

