package net.engine.input;

public class PointerInput extends BaseInput
{
  private PointerLocation location;

  public PointerInput(long nanoTime, PointerLocation location)
  {
    super(nanoTime);
    this.location = location;
  }

  public int getX()
  {
    return location.getX();
  }

  public int getY()
  {
    return location.getY();
  }

  public int getScreenX()
  {
    return location.getScreenX();
  }

  public int getScreenY()
  {
    return location.getScreenY();
  }
}

