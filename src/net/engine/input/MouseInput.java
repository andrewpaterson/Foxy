package net.engine.input;

public class MouseInput
    extends ButtonInput
{
  private PointerLocation location;

  public MouseInput(long nanoTime, boolean pressed, Button button, PointerLocation location)
  {
    super(nanoTime, pressed, button);
    this.location = location;
  }

  public int getButton()
  {
    return button.getVirtualCode();
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

