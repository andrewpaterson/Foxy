package net.engine.thread;

import java.util.ArrayList;
import java.util.List;

public class Job
{
  private List<Work> workList;
  private int takeSize;

  public Job(int takeSize)
  {
    this.takeSize = takeSize;
    this.workList = new ArrayList<>();
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
}

