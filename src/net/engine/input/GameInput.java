package net.engine.input;

import net.engine.collections.ArrayExtended;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 2016/08/09.
 */
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
    mouseLocation = MouseInfo.getPointerInfo().getLocation();
    mouseState = new ArrayExtended<>(Boolean.FALSE);
  }

  public void setMouseLocation()
  {
    mouseLocation = MouseInfo.getPointerInfo().getLocation();
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

  public void setMouseButtonState(int button, boolean state)
  {
    mouseState.set(button, state);
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

