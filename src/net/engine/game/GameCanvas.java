package net.engine.game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;


public class GameCanvas extends Canvas
{
  private BufferStrategy strategy;
  private Timer timer;
  private boolean frameBegun;
  private long tick;


  public GameCanvas()
  {
    setIgnoreRepaint(true);
    PaintEvent paintEvent = new PaintEvent(this);
    timer = new Timer(16, paintEvent);
    frameBegun = false;
    tick = 0;
  }

  public void start()
  {
    createBufferStrategy(2);
    strategy = getBufferStrategy();

    timer.start();
  }

  public synchronized void render()
  {
    if (!frameBegun)
    {
      Graphics graphics = strategy.getDrawGraphics();

      strategy.show();
      Toolkit.getDefaultToolkit().sync();

      graphics.dispose();
      tick++;
    }
  }

  public synchronized Graphics beginFrame()
  {
    frameBegun = true;
    return strategy.getDrawGraphics();
  }

  public synchronized void endFrame()
  {
    frameBegun = false;
  }

  public synchronized long getTick()
  {
    return tick;
  }
}

