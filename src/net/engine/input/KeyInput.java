package net.engine.input;

public class KeyInput
    extends ButtonInput
{
  public KeyInput(long nanoTime, boolean pressed, Button button)
  {
    super(nanoTime, pressed, button);
  }

  public int getKey()
  {
    return button.getVirtualCode();
  }
}

