package net.engine.cel;

import net.engine.IntegerRange;
import net.engine.math.Float2;
import net.engine.picture.Picture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CelHelper
{
  private List<Cel> cels;

  private CelHelper()
  {
    cels = new ArrayList<>();
  }

  public CelHelper(String fileName, boolean trim)
  {
    this();
    BufferedImage image = loadImage(fileName);
    addCel(image, trim);
  }

  public CelHelper(String fileName, int cellCountX, int cellCountY, boolean leftToRightFirst, boolean trim)
  {
    this();
    BufferedImage image = loadImage(fileName);
    breakIntoFrames(image, cellCountX, cellCountY, leftToRightFirst, trim);
  }

  public CelHelper(Image image, int cellCountX, int cellCountY, boolean leftToRightFirst, boolean trim)
  {
    this();
    breakIntoFrames(image, cellCountX, cellCountY, leftToRightFirst, trim);
  }

  public CelHelper(Picture picture, int cellCountX, int cellCountY, boolean leftToRightFirst, boolean trim)
  {
    this();
    BufferedImage image = convertFromPicture(picture);
    breakIntoFrames(image, cellCountX, cellCountY, leftToRightFirst, trim);
  }

  public CelHelper(Picture picture)
  {
    this();
    BufferedImage image = convertFromPicture(picture);
    addCel(image, true);
  }

  private BufferedImage loadImage(String fileName)
  {
    URL resource = getResource(fileName);
    if (resource != null)
    {
      return loadBufferedImage(resource);
    }
    return loadFileImage(fileName);
  }

  private BufferedImage loadFileImage(String fileName)
  {
    File file = new File("etc/image/" + fileName);
    try
    {
      return ImageIO.read(file);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  public List<Cel> getCels()
  {
    return cels;
  }

  private BufferedImage convertFromPicture(Picture picture)
  {
    BufferedImage image = new BufferedImage(picture.getWidth(), picture.getHeight(), BufferedImage.TYPE_INT_ARGB);

    int colour[] = new int[4];
    WritableRaster raster = image.getWritableTile(0, 0);
    for (int y = 0; y < picture.getHeight(); y++)
    {
      for (int x = 0; x < picture.getWidth(); x++)
      {
        int index = picture.getPixel(x, y);
        Color color = picture.getColour(index);

        if (color != null)
        {
          colour[3] = color.getAlpha();
          colour[0] = color.getRed();
          colour[1] = color.getGreen();
          colour[2] = color.getBlue();
          raster.setPixel(x, y, colour);
        }
      }
    }
    return image;
  }

  private BufferedImage loadBufferedImage(URL url)
  {
    try
    {
      BufferedImage source = ImageIO.read(url);
      if (source != null)
      {
        return source;
      }
      throw new RuntimeException("Couldn't load image [" + url.getPath() + "]");
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  private URL getResource(String fileName)
  {
    fileName = "/net/image/" + fileName;
    return getClass().getResource(fileName);
  }

  private List<Point> breakIntoFrames(Image source, int width, int height, int columnCount, int rowCount, int leftOffset, int topOffset, int verticalSpacing, int horizontalSpacing, boolean leftToRightFirst, boolean trim)
  {
    if ((columnCount == -1) && (width == -1))
    {
      width = source.getWidth(null);
      columnCount = 1;
    } else
    {
      if (columnCount == -1)
      {
        int top = source.getWidth(null) + leftOffset;
        int bottom = width + verticalSpacing;
        columnCount = top / bottom;

        if ((top % bottom) / width >= 1)
        {
          columnCount++;
        }
      }
      if (width == -1)
      {
        width = (source.getWidth(null)) / columnCount;
      }
    }
    if ((rowCount == -1) && (height == -1))
    {
      height = source.getHeight(null);
      rowCount = 1;
    } else
    {
      if (rowCount == -1)
      {
        int top = source.getHeight(null) + topOffset;
        int bottom = height + horizontalSpacing;
        rowCount = top / bottom;

        if ((top % bottom) / width >= 1)
        {
          rowCount++;
        }
      }
      if (height == -1)
      {
        height = (source.getHeight(null)) / rowCount;
      }
    }

    List<Point> grid = new ArrayList<>();
    if (leftToRightFirst)
    {
      for (int y = 0; y < rowCount; y++)
      {
        int sy = topOffset + y * (height + horizontalSpacing);
        for (int x = 0; x < columnCount; x++)
        {
          int sx = leftOffset + x * (width + verticalSpacing);
          grid.add(new Point(x, y));
          addCel(source, sx, sy, width, height, trim);
        }
      }
    } else
    {
      for (int x = 0; x < columnCount; x++)
      {
        int sx = leftOffset + x * (width + verticalSpacing);
        for (int y = 0; y < rowCount; y++)
        {
          int sy = topOffset + y * (height + horizontalSpacing);
          grid.add(new Point(x, y));
          addCel(source, sx, sy, width, height, trim);
        }
      }
    }
    return grid;
  }

  private Cel addCel(Image source, int sx, int sy, int width, int height, boolean trim)
  {
    Cel cel = newCel(convertToBufferedImage(source, 0, 0, sx, sy, width, height, Transparency.TRANSLUCENT), trim);
    cels.add(cel);
    return cel;
  }

  private void breakIntoFrames(Image source, int cellCountX, int cellCountY, boolean leftToRightFirst, boolean trim)
  {
    int width = source.getWidth(null) / cellCountX;
    int height = source.getHeight(null) / cellCountY;

    breakIntoFrames(source, width, height, cellCountX, cellCountY, 0, 0, 0, 0, leftToRightFirst, trim);
  }

  private void addCel(Image image, boolean trim)
  {
    int height = image.getHeight(null);
    int width = image.getWidth(null);
    addCel(image, 0, 0, width, height, trim);
  }

  private BufferedImage convertToBufferedImage(Image image, int dx, int dy, int sx, int sy, int width, int height, int transparency)
  {
    GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();

    BufferedImage copy = graphicsConfiguration.createCompatibleImage(width, height, transparency);
    Graphics2D g2d = copy.createGraphics();
    g2d.drawImage(image, dx, dy, dx + width, dy + height, sx, sy, sx + width, sy + height, null);
    g2d.dispose();
    return copy;
  }

  private GraphicsConfiguration getGraphicsConfiguration()
  {
    GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    return graphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration();
  }

  public Cel get(int frame)
  {
    if ((frame < 0) || (frame > cels.size()))
    {
      return null;
    }
    Cel cel = cels.get(frame);
    if (cel != null)
    {
      return cel;
    }
    return null;
  }

  public CelHelper addFlippedFrames(boolean horizontal, boolean vertical, Object... frames)
  {
    if (frames.length == 0)
    {
      return this;
    }

    for (Object object : frames)
    {
      if (object instanceof Integer)
      {
        addFlippedFrame((Integer) object, horizontal, vertical);
      } else if (object instanceof IntegerRange)
      {
        IntegerRange integerRange = (IntegerRange) object;
        for (int frame = integerRange.getMin(); frame <= integerRange.getMax(); frame++)
        {
          addFlippedFrame(frame, horizontal, vertical);
        }
      }
    }
    return this;
  }

  private int addFlippedFrame(int sourceFrame, boolean horizontal, boolean vertical)
  {
    Cel cel = cels.get(sourceFrame);
    if (cel == null)
    {
      cels.add(null);
      return cels.size() - 1;
    }

    int imageWidth;
    int imageHeight;

    BufferedImage bufferedImage = cel.getBufferedImage();

    imageWidth = bufferedImage.getWidth();
    imageHeight = bufferedImage.getHeight();

    GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();
    BufferedImage newFrame = graphicsConfiguration.createCompatibleImage(imageWidth, imageHeight, bufferedImage.getType());
    Graphics2D g2d = newFrame.createGraphics();

    int dx1, dy1, dx2, dy2;
    float top, left, bottom, right;
    if (horizontal)
    {
      dx1 = imageWidth;
      dx2 = 0;
      left = cel.getOffsetBottomRight().x;
      right = cel.getOffsetTopLeft().x;
    } else
    {
      dx1 = 0;
      dx2 = imageWidth;
      left = cel.getOffsetTopLeft().x;
      right = cel.getOffsetBottomRight().x;
    }

    if (vertical)
    {
      dy1 = imageHeight;
      dy2 = 0;
      top = cel.getOffsetBottomRight().y;
      bottom = cel.getOffsetTopLeft().y;
    } else
    {
      dy1 = 0;
      dy2 = imageHeight;
      top = cel.getOffsetTopLeft().y;
      bottom = cel.getOffsetBottomRight().y;
    }

    g2d.drawImage(cel.getBufferedImage(), dx1, dy1, dx2, dy2, 0, 0, imageWidth, imageHeight, null);

    Cel newCel = new Cel(newFrame, cel.getHorizontalAlignment(), cel.getVerticalAlignment(), new Float2(left, top), new Float2(right, bottom));
    cels.add(newCel);

    return cels.size() - 1;
  }

  public CelHelper offsetFrames(float left, float top, float right, float bottom)
  {
    for (Cel cel : cels)
    {
      if (cel != null)
      {
        cel.offset(left, top, right, bottom);
      }
    }
    return this;
  }

  private Cel newCel(BufferedImage bufferedImage, boolean trim)
  {
    return newCel(bufferedImage, Cel.CENTERED, Cel.CENTERED, trim);
  }

  private Cel newCel(BufferedImage bufferedImage, int horizontalAlignment, int verticalAlignment, boolean trim)
  {
    if (trim)
    {
      CelTrimmer celTrimmer = new CelTrimmer(bufferedImage);

      if (celTrimmer.getBufferedImage() != null)
      {
        return new Cel(celTrimmer.getBufferedImage(), horizontalAlignment, verticalAlignment, celTrimmer.getOffsetTopLeft(), celTrimmer.getOffsetBottomRight());
      } else
      {
        return null;
      }
    } else
    {
      return new Cel(bufferedImage, horizontalAlignment, verticalAlignment, new Float2(0, 0), new Float2(0, 0));
    }
  }
}

