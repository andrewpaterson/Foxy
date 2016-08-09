package net.foxy.loading;

import net.engine.cel.Cel;
import net.engine.cel.CelHelper;
import net.engine.cel.CelStore;
import net.engine.initial.Loader;

import java.awt.*;

public class FoxyLoader extends Loader
{
  private CelStore celStore;

  public FoxyLoader(CelStore celStore)
  {
    super();
    this.celStore = celStore;
  }

  @Override
  public void load()
  {
    celStore.addCels("Start Text", new CelHelper(new Font("Verdana", Font.PLAIN, 35), Color.RED, "Start").setAlignment(Cel.LEFT_ALIGNED, Cel.TOP_ALIGNED).getCels());

    try
    {
      Thread.sleep(1500);
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
}

