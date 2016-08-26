package net.engine.thread;

import java.util.ArrayList;
import java.util.List;

public class WorkQueue
{
  private List<Work> workList;
  private int index;
  private int length;

  public WorkQueue()
  {
    workList = new ArrayList<>();
    index = -1;
  }

  public synchronized Work take()
  {
    if (index < length - 1)
    {
      index++;
      return workList.get(index);
    }
    return null;
  }

  public void add(Work work)
  {
    if (length < workList.size())
    {
      this.workList.set(length, work);
    }
    else
    {
      this.workList.add(work);
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

  public synchronized void clear()
  {
    for (int i = 0; i < workList.size(); i++)
    {
      workList.set(i, null);
    }

    length = 0;
    index = -1;
  }
}

