package net.engine.collections;

import java.util.ArrayList;
import java.util.Collection;

public class ArrayExtended<E>
    extends ArrayList<E>
{
  private E defaultValue;

  public ArrayExtended(E defaultValue)
  {
    super();
    this.defaultValue = defaultValue;
  }

  public ArrayExtended(Collection<? extends E> c, E defaultValue)
  {
    super(c);
    this.defaultValue = defaultValue;
  }

  public E set(int index, E e)
  {
    if (index >= size())
    {
      int numToAdd = index - size();
      for (int i = 0; i < numToAdd; i++)
      {
        this.add(defaultValue);
      }
      add(e);
      return null;
    }
    return super.set(index, e);
  }

  public E safeGet(int index)
  {
    if (index >= size())
    {
      return defaultValue;
    }
    return super.get(index);
  }
}

