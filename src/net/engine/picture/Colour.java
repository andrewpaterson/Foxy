package net.engine.picture;

import java.awt.*;

public class Colour
{
  public static int getRed(int argb)
  {
    return (argb >> 16) & 0xFF;
  }

  public static int getGreen(int argb)
  {
    return (argb >> 8) & 0xFF;
  }

  public static int getBlue(int argb)
  {
    return argb & 0xFF;
  }

  public static int getAlpha(int argb)
  {
    return (argb >> 24) & 0xff;
  }

  public static int getARGB(Color color)
  {
    return ((color.getAlpha() & 0xFF) << 24) |
            ((color.getRed() & 0xFF) << 16) |
            ((color.getGreen() & 0xFF) << 8) |
            ((color.getBlue() & 0xFF));
  }

  public static int getARGB(float alpha, float red, float green, float blue)
  {
    return (((int)(alpha * 255) & 0xFF) << 24) |
            (((int)(red * 255) & 0xFF) << 16) |
            (((int)(green * 255) & 0xFF) << 8) |
            (((int)(blue * 255) & 0xFF));
  }
}

