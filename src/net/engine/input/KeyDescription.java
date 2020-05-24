package net.engine.input;

import java.util.ArrayList;
import java.util.List;

public class KeyDescription
{
  private List<String> locationDescription;

  public KeyDescription()
  {
    locationDescription = new ArrayList<>(4);
    for (int i = 0; i < 4; i++)
    {
      locationDescription.add(null);
    }
  }

  public void setDescription(int keyLocation, String description)
  {
    if (keyLocation >= 1 && keyLocation <= 4)
    {
      locationDescription.set(keyLocation - 1, description);
    }
  }

  public String get(int keyLocation)
  {
    if (keyLocation >= 1 && keyLocation <= 4)
    {
      return locationDescription.get(keyLocation - 1);
    }
    return null;
  }
}

