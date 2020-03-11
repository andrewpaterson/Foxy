package net.engine.scene;

public class Camera
    extends Movable
{
  public Camera setPosition(int x, int y)
  {
    position.set(x, y);
    return this;
  }
}

