package net.foxy.loading;

import net.engine.initial.Loader;

public class FoxyLoader extends Loader
{
  @Override
  public void load()
  {
    try
    {
      Thread.sleep(2000);
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
}

