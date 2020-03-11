package net.engine.input;

public class WheelInput
    extends BaseInput
{
  private float rotation;

  public WheelInput(long nanoTime, float rotation)
  {
    super(nanoTime);
    this.rotation = rotation;
  }

  public float getRotation()
  {
    return rotation;
  }
}

