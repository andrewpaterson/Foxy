package net.engine.picture.util;

import net.engine.common.EngineException;
import net.engine.picture.Picture;
import net.engine.thread.Job;
import net.engine.thread.Threadanator;
import net.engine.thread.util.CopyJob;

public class Speckle
{
  public static void speckle(Picture picture, int amount)
  {
    if (amount > 0)
    {
      Object data2 = picture.createData();

      Job copyJob1;
      if (data2 instanceof int[])
      {
        copyJob1 = CopyJob.copy((int[]) data2, (int[]) picture.getData());
      }
      else if (data2 instanceof byte[])
      {
        copyJob1 = CopyJob.copy((byte[]) data2, (byte[]) picture.getData());
      }
      else
      {
        throw new EngineException();
      }
      Threadanator.getInstance().process(copyJob1);

      Job speckleJob = new Job(16);
      int height = picture.getHeight();
      int width = picture.getWidth();
      for (int y = 0; y < height; y++)
      {
        speckleJob.add(new SpeckleWork(width, amount, picture, data2, y));
      }
      Threadanator.getInstance().process(speckleJob);

      Job copyJob2;
      if (data2 instanceof int[])
      {
        copyJob2 = CopyJob.copy((int[]) picture.getData(), (int[]) data2);
      }
      else
      {
        copyJob2 = CopyJob.copy((byte[]) picture.getData(), (byte[]) data2);
      }
      Threadanator.getInstance().process(copyJob2);
    }
  }
}

