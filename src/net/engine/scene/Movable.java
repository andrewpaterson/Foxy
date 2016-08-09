package net.engine.scene;

import net.engine.math.Float2;
import net.engine.math.Int2;

/**
 * Created by andrew on 2016/08/09.
 */
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

