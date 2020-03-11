package net.foxy;

import net.engine.cel.CelStore;
import net.engine.game.Stage;

public abstract class FoxyStage
    extends Stage
{
  protected CelStore celStore;

  public FoxyStage(CelStore celStore)
  {
    this.celStore = celStore;
  }
}

