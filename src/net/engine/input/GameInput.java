package net.engine.input;

import net.engine.collections.ArrayExtended;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameInput
{
  protected List<BaseInput> events;
  protected ArrayExtended<Boolean> keyboardState;
  protected PointerLocation pointerLocation;
  protected ArrayExtended<Boolean> mouseState;

  public GameInput()
  {
    events = new ArrayList<>();
    keyboardState = new ArrayExtended<>(Boolean.FALSE);
    pointerLocation = null;
    mouseState = new ArrayExtended<>(Boolean.FALSE);
  }

  public void setPointerLocation(PointerLocation pointerLocation)
  {
    this.pointerLocation = pointerLocation;
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

  public PointerLocation getPointerLocation()
  {
    return pointerLocation;
  }

  public void setMouseButtonState(int button, boolean state)
  {
    mouseState.set(button, state);
  }

  public void addInput(BaseInput input)
  {
    events.add(input);
  }

  public List<BaseInput> popEvents()
  {
    List<BaseInput> local = events;
    events = new ArrayList<>();
    return local;
  }

  public void processEvents(InputHandler inputHandler)
  {
    List<BaseInput> events = popEvents();
    if (inputHandler != null)
    {
//    for (InputEvent event : events)
//    {
//
//    }
    }
  }
}

