package net.engine.game;

import net.engine.input.GameInput;

import java.awt.*;
import java.awt.event.*;

public class InputAdapter
        implements
        WindowListener,
        MouseMotionListener,
        MouseWheelListener,
        KeyListener,
        MouseListener,
        ComponentListener

{
  private GameInput input;

  public InputAdapter(Window window, Component component, GameInput input) throws HeadlessException
  {
    this.input = input;
    window.addWindowListener(this);
    component.addMouseMotionListener(this);
    component.addMouseWheelListener(this);
    component.addKeyListener(this);
    component.addMouseListener(this);
    component.addComponentListener(this);
  }

  @Override
  public void windowClosing(WindowEvent e)
  {
    System.exit(0);
  }

  @Override
  public void mouseMoved(MouseEvent e)
  {
    input.addMouseMovedEvent(e);
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e)
  {
    input.addMouseWheelEvent(e);
  }

  @Override
  public void keyPressed(KeyEvent e)
  {
    input.addKeyEvent(e);
    input.setKeyState(e.getKeyCode(), true);
  }

  @Override
  public void keyReleased(KeyEvent e)
  {
    input.addKeyEvent(e);
    input.setKeyState(e.getKeyCode(), false);
  }

  @Override
  public void mousePressed(MouseEvent e)
  {
    input.addMouseEvent(e);
    input.setMouseButtonState(e.getButton(), true);
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
    input.addMouseEvent(e);
    input.setMouseButtonState(e.getButton(), false);
  }

  public void windowOpened(WindowEvent e)
  {
  }

  public void windowClosed(WindowEvent e)
  {
  }

  public void windowIconified(WindowEvent e)
  {
  }

  public void windowDeiconified(WindowEvent e)
  {
  }

  public void windowActivated(WindowEvent e)
  {
  }

  public void windowDeactivated(WindowEvent e)
  {
  }

  public void mouseDragged(MouseEvent e)
  {
  }

  public void keyTyped(KeyEvent e)
  {
  }

  public void mouseClicked(MouseEvent e)
  {
  }

  public void mouseEntered(MouseEvent e)
  {
  }

  public void mouseExited(MouseEvent e)
  {
  }

  public void componentResized(ComponentEvent e)
  {
  }

  public void componentMoved(ComponentEvent e)
  {
  }

  public void componentShown(ComponentEvent e)
  {
  }

  public void componentHidden(ComponentEvent e)
  {
  }
}

