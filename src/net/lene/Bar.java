package net.lene;

import net.engine.picture.Picture;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Lautus Solutions.
 */
public abstract class Bar
{
  protected List<Integer> colours;

  public Bar( int... colours)
  {
    this.colours = new ArrayList<>(colours.length);
    for (int colour : colours)
    {
      this.colours.add(colour);
    }
  }

  public abstract void tick(double time, Picture picture);
}

