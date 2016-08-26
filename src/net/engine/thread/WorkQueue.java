package net.engine.thread;

import java.util.ArrayList;
import java.util.List;

public class WorkQueue
{
  private List<Work> currentWork;
  private List<Work> previousWork;
  private int index;
  private int length;

  public WorkQueue()
  {
    currentWork = new ArrayList<>();
    previousWork = new ArrayList<>();
    index = -1;
  }

  public synchronized Work take()
  {
    if (index < length - 1)
    {
      index++;
      return currentWork.get(index);
    }
    return null;
  }

  public synchronized void swap()
  {
    clear();

    List<Work> temp = currentWork;
    currentWork = previousWork;
    previousWork = temp;
  }

  public void clear()
  {
    length = 0;
    index = -1;
    for (int i = 0; i < currentWork.size(); i++)
    {
      currentWork.set(i, null);
    }
  }

  public void add(Work work)
  {
    if (length < this.currentWork.size())
    {
      this.currentWork.set(length, work);
    }
    else
    {
      this.currentWork.add(work);
    }
    length++;
  }

  public int size()
  {
    return length - (index + 1);
  }

  public synchronized boolean isEmpty()
  {
    return size() == 0;
  }
}

