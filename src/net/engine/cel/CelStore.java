package net.engine.cel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CelStore
{
  private static CelStore instance = null;
  private Map<String, List<Cel>> celHelpers;

  public static CelStore getInstance()
  {
    if (instance == null)
    {
      instance = new CelStore();
    }
    return instance;
  }

  public CelStore()
  {
    celHelpers = new LinkedHashMap<>();
  }

  public List<Cel> get(String name)
  {
    return celHelpers.get(name);
  }

  public void addCels(String name, List<Cel> cels)
  {
    List<Cel> storedCelHelper = celHelpers.get(name);
    if (storedCelHelper == null)
    {
      celHelpers.put(name, cels);
    }
  }
}

