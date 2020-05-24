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
    length = 0;
  }

  public synchronized int take(int takeSize, Work[] returnList)
  {
    for (int i = 0; i < takeSize; i++)
    {
      if (index < length - 1)
      {
        index++;
        Work work = workList.get(index);
        returnList[i] = work;
        workList.set(index, null);
      }
      else
      {
        return i;
      }
    }
    return takeSize;
  }

  public void add(Work work)
  {
    if (work == null)
    {
      return;
    }

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

  public boolean isCleared()
  {
    return length == 0 && index == -1;
  }

  public synchronized boolean isEmpty()
  {
    return size() == 0;
  }

  public synchronized void clear()
  {
    length = 0;
    index = -1;
  }
}

