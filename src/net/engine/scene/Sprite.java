package net.engine.scene;

import net.engine.cel.Cel;
import net.engine.collections.ArrayExtended;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Sprite
    extends Movable
{
  protected ArrayExtended<Cel> cels;
  protected int celFrame;
  protected int layer;

  public Sprite(List<Cel> cels)
  {
    this.cels = new ArrayExtended<>(cels, null);
    this.celFrame = 0;
    this.layer = 0;
  }

  public Sprite(Cel cel)
  {
    this.cels = new ArrayExtended<>(null);
    this.cels.add(cel);

    this.celFrame = 0;
    this.layer = 0;
  }

  public Cel getCel()
  {
    return cels.get(celFrame);
  }

  public int getGraphicsLeft()
  {
    return getCel().getGraphicsLeft() + position.x;
  }

  public int getGraphicsTop()
  {
    return getCel().getGraphicsTop() + position.y;
  }

  public int getGraphicsRight()
  {
    return getCel().getGraphicsRight() + position.x;
  }

  public int getGraphicsBottom()
  {
    return getCel().getGraphicsBottom() + position.y;
  }

  public BufferedImage getBufferedImage()
  {
    return getCel().getBufferedImage();
  }

  public Sprite setLayer(int layer)
  {
    this.layer = layer;
    return this;
  }

  public Sprite setFrame(int celFrame)
  {
    this.celFrame = celFrame;
    return this;
  }

  public void addCels(java.util.List<Cel> celList)
  {
    cels.addAll(celList);
  }

  public void addCels(java.util.List<Cel> celList, Integer... celOrder)
  {
    for (Integer index : celOrder)
    {
      Cel cel = celList.get(index);
      cels.add(cel);
    }
  }

  public Sprite setPosition(int x, int y)
  {
    position.set(x, y);
    return this;
  }

  public boolean isVisible()
  {
    return getCel() != null;
  }

  public void render(Graphics graphics, Camera camera)
  {
    if (isVisible())
    {
      int x = getGraphicsLeft(camera);
      int y = getGraphicsTop(camera);
      graphics.drawImage(getBufferedImage(), x, y, null);
    }
  }

  public int getGraphicsLeft(Camera camera)
  {
    return getGraphicsLeft() - camera.getPosition().x;
  }

  public int getGraphicsTop(Camera camera)
  {
    return getGraphicsTop() - camera.getPosition().y;
  }

  public int getGraphicsRight(Camera camera)
  {
    return getGraphicsRight() - camera.getPosition().x;
  }

  public int getGraphicsBottom(Camera camera)
  {
    return getGraphicsBottom() - camera.getPosition().y;
  }

  public int getLeft(Camera camera)
  {
    return getCel().getLeft() + position.x;
  }

  public int getTop(Camera camera)
  {
    return getCel().getTop() + position.y;
  }

  public int getWidth()
  {
    return getCel().getWidth();
  }

  public int getHeight()
  {
    return getCel().getHeight();
  }

  public Rectangle toRectangle(Camera camera)
  {
    return new Rectangle(getLeft(camera), getTop(camera), getWidth(), getHeight());
  }
}

