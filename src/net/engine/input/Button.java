package net.engine.input;

public class Button
{
  private int virtualKey;
  private int location;
  private String description;
  private boolean symbol;
  private char display;

  public Button(int virtualKey, int location, String description, boolean symbol, char display)
  {
    this.virtualKey = virtualKey;
    this.location = location;
    this.description = description;
    this.symbol = symbol;
    this.display = display;
  }

  public int getVirtualCode()
  {
    return virtualKey;
  }

  public int getLocation()
  {
    return location;
  }

  public String getDescription()
  {
    return description;
  }

  public boolean isSymbol()
  {
    return symbol;
  }

  public char getDisplay()
  {
    return display;
  }
}

