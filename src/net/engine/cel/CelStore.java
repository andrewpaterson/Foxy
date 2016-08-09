package net.engine.cel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CelStore
{
  private Map<String, List<Cel>> cels;

  public CelStore()
  {
    cels = new LinkedHashMap<>();
  }

  public List<Cel> get(String name)
  {
    return cels.get(name);
  }

  public void addCels(String name, List<Cel> cels)
  {
    List<Cel> storedCelHelper = this.cels.get(name);
    if (storedCelHelper == null)
    {
      this.cels.put(name, cels);
    }
  }
}

