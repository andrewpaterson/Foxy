package net.engine.picture.palette;

import net.engine.picture.ColourGradient;

import java.awt.*;

/**
 * Copyright (c) Lautus Solutions.
 */
public class GreyPalette
{
  public static Color[] create()
  {
    Color[] palette = new Color[256];
    ColourGradient.generate(new Color(0, 0, 0), new Color(255, 255, 255), palette, 0, 255);
    return palette;
  }
}

