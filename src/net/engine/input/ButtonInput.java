package net.engine.input;

public class ButtonInput
    extends BaseInput
{
  protected boolean pressed;
  protected Button button;

  public ButtonInput(long nanoTime, boolean pressed, Button button)
  {
    super(nanoTime);
    this.pressed = pressed;
    this.button = button;
  }

  public boolean isPressed()
  {
    return pressed;
  }

  public boolean isReleased()
  {
    return !pressed;
  }

  public int getLocation()
  {
    return button.getLocation();
  }

  public String getDescription()
  {
    return button.getDescription();
  }

  public boolean isSymbol()
  {
    return button.isSymbol();
  }

  public char getDisplay()
  {
    return button.getDisplay();
  }

  public String getState()
  {
    if (pressed)
    {
      return "Pressed";
    }
    else
    {
      return "Released";
    }
  }
}

