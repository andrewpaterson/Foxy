package net.engine.cel;

import net.engine.math.Float2;

import java.awt.image.BufferedImage;

public class Cel
{
  public static final int LEFT_ALIGNED = 0;
  public static final int CENTERED = 1;
  public static final int RIGHT_ALIGNED = 2;
  public static final int TOP_ALIGNED = 3;
  public static final int BOTTOM_ALIGNED = 4;

  private BufferedImage bufferedImage;
  private Float2 offsetTopLeft;
  private Float2 offsetBottomRight;

  private int horizontalAlignment;
  private int verticalAlignment;

  public Cel(BufferedImage bufferedImage, int horizontalAlignment, int verticalAlignment, Float2 offsetTopLeft, Float2 offsetBottomRight)
  {
    this.bufferedImage = bufferedImage;
    this.horizontalAlignment = horizontalAlignment;
    this.verticalAlignment = verticalAlignment;
    this.offsetTopLeft = offsetTopLeft;
    this.offsetBottomRight = offsetBottomRight;
  }

  public Cel(Cel source)
  {
    bufferedImage = source.bufferedImage;
    offsetTopLeft = source.offsetTopLeft;
    offsetBottomRight = source.offsetBottomRight;

    horizontalAlignment = source.horizontalAlignment;
    verticalAlignment = source.verticalAlignment;
  }

  public void setHorizontalAlignment(int horizontalAlignment)
  {
    this.horizontalAlignment = horizontalAlignment;
  }

  public void setVerticalAlignment(int verticalAlignment)
  {
    this.verticalAlignment = verticalAlignment;
  }

  public float getGraphicsLeft()
  {
    int width = bufferedImage.getWidth();
    if (horizontalAlignment == LEFT_ALIGNED)
    {
      return offsetTopLeft.x;
    } else if (horizontalAlignment == CENTERED)
    {
      return ((offsetTopLeft.x + width + offsetBottomRight.x) / 2) - width - offsetBottomRight.x;
    } else if (horizontalAlignment == RIGHT_ALIGNED)
    {
      return -offsetBottomRight.x - width;
    }
    return 0;
  }

  public float getGraphicsTop()
  {
    int height = bufferedImage.getHeight();
    if (verticalAlignment == TOP_ALIGNED)
    {
      return offsetTopLeft.y;
    } else if (verticalAlignment == CENTERED)
    {
      return ((offsetTopLeft.y + height + offsetBottomRight.y) / 2) - height - offsetBottomRight.y;
    } else if (verticalAlignment == BOTTOM_ALIGNED)
    {
      return -offsetTopLeft.y - height;
    }
    return 0;
  }

  public float getGraphicsBottom()
  {
    int height = bufferedImage.getHeight();
    if (verticalAlignment == TOP_ALIGNED)
    {
      return offsetBottomRight.y + height;
    } else if (verticalAlignment == CENTERED)
    {
      return ((offsetBottomRight.y + height + offsetTopLeft.y) / 2) - offsetBottomRight.y;
    } else if (verticalAlignment == BOTTOM_ALIGNED)
    {
      return offsetBottomRight.y;
    }
    return 0;
  }

  public float getGraphicsRight()
  {
    int width = bufferedImage.getWidth();
    if (horizontalAlignment == LEFT_ALIGNED)
    {
      return offsetBottomRight.x + width;
    }
    if (horizontalAlignment == CENTERED)
    {
      return ((offsetBottomRight.x + width + offsetTopLeft.x) / 2) - offsetBottomRight.x;
    }
    if (horizontalAlignment == RIGHT_ALIGNED)
    {
      return offsetBottomRight.x;
    }
    return 0;
  }

  public float getRelativeLeft()
  {
    int width = bufferedImage.getWidth();
    if (horizontalAlignment == LEFT_ALIGNED)
    {
      return 0;
    } else if (horizontalAlignment == CENTERED)
    {
      return (offsetTopLeft.x + width + offsetBottomRight.x) / 2;
    } else if (horizontalAlignment == RIGHT_ALIGNED)
    {
      return offsetTopLeft.x + width + offsetBottomRight.x;
    }
    return 0;
  }

  public float getRelativeTop()
  {
    int height = bufferedImage.getHeight();
    if (verticalAlignment == TOP_ALIGNED)
    {
      return 0;
    } else if (verticalAlignment == CENTERED)
    {
      return (offsetTopLeft.y + height + offsetBottomRight.y) / 2;
    } else if (verticalAlignment == BOTTOM_ALIGNED)
    {
      return offsetTopLeft.y + height + offsetBottomRight.y;
    }
    return 0;
  }

  public void setAlignment(int horizontalAlignment, int verticalAlignment)
  {
    setHorizontalAlignment(horizontalAlignment);
    setVerticalAlignment(verticalAlignment);
  }

  public void setOffset(float left, float top, float right, float bottom)
  {
    offsetTopLeft.x = left;
    offsetTopLeft.y = top;
    offsetBottomRight.x = right;
    offsetBottomRight.y = bottom;
  }

  public void offset(float left, float top, float right, float bottom)
  {
    offsetTopLeft.x += left;
    offsetTopLeft.y += top;
    offsetBottomRight.x += right;
    offsetBottomRight.y += bottom;
  }

  public BufferedImage getBufferedImage()
  {
    return bufferedImage;
  }

  public Float2 getOffsetTopLeft()
  {
    return offsetTopLeft;
  }

  public Float2 getOffsetBottomRight()
  {
    return offsetBottomRight;
  }

  public int getHorizontalAlignment()
  {
    return horizontalAlignment;
  }

  public int getVerticalAlignment()
  {
    return verticalAlignment;
  }
}

