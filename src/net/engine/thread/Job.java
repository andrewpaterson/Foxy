package net.engine.thread;

import java.util.ArrayList;
import java.util.List;

public class Job
{
  private List<Work> workList;
  private int takeSize;
  private boolean multiThreaded;

  public Job(int takeSize)
  {
    this(true, takeSize);
  }

  public Job(boolean multiThreaded, int takeSize)
  {
    this.takeSize = takeSize;
    this.workList = new ArrayList<>();
    this.multiThreaded = multiThreaded;
  }

  public void add(Work work)
  {
    workList.add(work);
  }

  public List<Work> getWorkList()
  {
    return workList;
  }

  public int getTakeSize()
  {
    return takeSize;
  }

  public boolean isMultiThreaded()
  {
    return multiThreaded;
  }
}

