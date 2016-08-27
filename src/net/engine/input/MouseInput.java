package net.engine.input;

public class MouseInput extends ButtonInput
{
  public MouseInput(long nanoTime, boolean pressed, Button button)
  {
    super(nanoTime, pressed, button);
  }

  public int getButton()
  {
    return button.getVirtualCode();
  }
}

