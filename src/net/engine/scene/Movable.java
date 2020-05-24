package net.engine.scene;

import net.engine.math.Int2;

public class Movable
{
  protected Int2 position;

  public Movable()
  {
    position = new Int2();
  }

  public Int2 getPosition()
  {
    return position;
  }
}

