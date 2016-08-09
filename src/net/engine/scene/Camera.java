package net.engine.scene;

/**
 * Created by andrew on 2016/08/09.
 */
public class Camera extends Movable
{
  public Camera setPosition(int x, int y)
  {
    position.set(x, y);
    return this;
  }
}

