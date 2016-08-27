package net.engine.input;

public class ButtonInput extends BaseInput
{
  protected boolean pressed;
  protected Button button;

  public ButtonInput(long nanoTime, boolean pressed, Button button)
  {
    super(nanoTime);
    this.pressed = pressed;
    this.button = button;
  }

  public boolean pressed()
  {
    return pressed;
  }

  public boolean released()
  {
    return !pressed;
  }
}

