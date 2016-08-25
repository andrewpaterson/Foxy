package net.engine.game;

import java.awt.*;
import java.awt.image.BufferStrategy;


public class GameCanvas extends Canvas
{
  private BufferStrategy strategy;

  public GameCanvas()
  {
    setIgnoreRepaint(true);
  }

  public void start()
  {
    createBufferStrategy(2);
    strategy = getBufferStrategy();
  }

  public synchronized void render()
  {
      Graphics graphics = strategy.getDrawGraphics();

      strategy.show();
      Toolkit.getDefaultToolkit().sync();

      graphics.dispose();
  }

  public Graphics beginFrame()
  {
    return strategy.getDrawGraphics();
  }

  public void endFrame()
  {
  }
}

