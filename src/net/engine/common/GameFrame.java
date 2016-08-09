package net.engine.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GameFrame extends JFrame implements WindowListener
{
  private GameCanvas canvas;

  public GameFrame(String title) throws HeadlessException
  {
    super(title);

    canvas = new GameCanvas();
    add(canvas, BorderLayout.CENTER);
    setSize(600, 400);
    addWindowListener(this);
  }

  public GameCanvas getCanvas()
  {
    return canvas;
  }

  @Override
  public void windowOpened(WindowEvent e)
  {
  }

  @Override
  public void windowClosing(WindowEvent e)
  {
    System.exit(0);
  }

  @Override
  public void windowClosed(WindowEvent e)
  {
  }

  @Override
  public void windowIconified(WindowEvent e)
  {
  }

  @Override
  public void windowDeiconified(WindowEvent e)
  {
  }

  @Override
  public void windowActivated(WindowEvent e)
  {
  }

  @Override
  public void windowDeactivated(WindowEvent e)
  {
  }
}

