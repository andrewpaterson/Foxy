package net.engine.picture.util;

import net.engine.global.GlobalRandom;
import net.engine.picture.Picture;
import net.engine.thread.Work;

import java.util.Random;

public class SpeckleWork
    extends Work
{
  private int width;
  private Random random;
  private int amount;
  private Picture picture;
  private Object data2;
  private int y;

  public SpeckleWork(int width, int amount, Picture picture, Object data2, int y)
  {
    this.width = width;
    this.random = new Random(GlobalRandom.random.nextInt());
    this.amount = amount;
    this.picture = picture;
    this.data2 = data2;
    this.y = y;
  }

  @Override
  public void work()
  {
    for (int x = 0; x < width; x++)
    {
      int pixel = picture.getPixel(x + random.nextInt(amount * 2 + 1) - amount, y + random.nextInt(amount * 2 + 1) - amount);
      if (pixel != -1)
      {
        picture.setPixel(data2, x, y, pixel);
      }
    }
  }
}
