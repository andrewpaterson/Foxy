package net.engine.input;

import net.engine.collections.ArrayExtended;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

public class GameInput
{
  protected List<InputEvent> events;
  protected ArrayExtended<Boolean> keyboardState;
  protected Point mouseLocation;
  protected ArrayExtended<Boolean> mouseState;

  public GameInput()
  {
    events = new ArrayList<>();
    keyboardState = new ArrayExtended<>(Boolean.FALSE);
    mouseLocation = null;
    mouseState = new ArrayExtended<>(Boolean.FALSE);
  }

  public void setMouseLocation(Point mousePosition)
  {
    mouseLocation = mousePosition;
  }

  public boolean getKeyState(int key)
  {
    return keyboardState.safeGet(key);
  }

  public void setKeyState(int key, boolean state)
  {
    keyboardState.set(key, state);
  }

  public boolean getMouseButtonState(int button)
  {
    return mouseState.safeGet(button);
  }

  public Point getMouseLocation()
  {
    return mouseLocation;
  }

  public void setMouseButtonState(int button, boolean state)
  {
    int realButton = button;
    if (button == 1)
    {
      realButton = 0;
    }
    else if (button == 3)
    {
      realButton = 1;
    }
    mouseState.set(realButton, state);
  }

  public void addKeyEvent(KeyEvent keyEvent)
  {
    events.add(keyEvent);
  }

  public void addMouseWheelEvent(MouseWheelEvent mouseWheelEvent)
  {
    events.add(mouseWheelEvent);
  }

  public void addMouseMovedEvent(MouseEvent mouseEvent)
  {
    events.add(mouseEvent);
  }

  public void addMouseEvent(MouseEvent mouseEvent)
  {
    events.add(mouseEvent);
  }

  public List<InputEvent> popEvents()
  {
    List<InputEvent> local = events;
    events = new ArrayList<>();
    return local;
  }
}

