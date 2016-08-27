package net.engine.input;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedHashMap;
import java.util.Map;

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
  private Map<Integer, KeyDescription> keyDescriptions;

  public InputAdapter(Window window, Component component, GameInput input) throws HeadlessException
  {
    this.input = input;
    this.keyDescriptions = new LinkedHashMap<>();
    window.addWindowListener(this);
    component.addMouseMotionListener(this);
    component.addMouseWheelListener(this);
    component.addKeyListener(this);
    component.addMouseListener(this);
    component.addComponentListener(this);
  }

  private String getKeyDescription(int keyCode, int keyLocation)
  {
    KeyDescription keyDescription = keyDescriptions.get(keyCode);
    if (keyDescription == null)
    {
      keyDescription = new KeyDescription();
      keyDescriptions.put(keyCode, keyDescription);
    }

    String description = keyDescription.get(keyLocation);
    if (description == null)
    {
      String keyText = KeyEvent.getKeyText(keyCode);
      if (keyLocation == KeyEvent.KEY_LOCATION_LEFT)
      {
        keyText = "Left " + keyText;
      }
      else if (keyLocation == KeyEvent.KEY_LOCATION_RIGHT)
      {
        keyText = "Right " + keyText;
      }
      else if (keyLocation == KeyEvent.KEY_LOCATION_NUMPAD)
      {
        if (keyCode != KeyEvent.VK_NUM_LOCK)
        {
          keyText = keyText.replace("NumPad-", "");
          keyText = keyText.replace("NumPad ", "");
          keyText = "NumPad " + keyText;
        }
      }

      keyDescription.setDescription(keyLocation, keyText);
      description = keyText;
    }
    return description;
  }

  private boolean isSymbol(KeyEvent e)
  {
    return e.getKeyChar() != KeyEvent.CHAR_UNDEFINED;
  }

  private Button getKeyButton(KeyEvent e)
  {
    int keyCode = e.getKeyCode();
    int keyLocation = e.getKeyLocation();
    String description = getKeyDescription(keyCode, keyLocation);
    boolean symbol = isSymbol(e);
    return new Button(keyCode, keyLocation, description, symbol, e.getKeyChar());
  }

  private Button getMouseButton(MouseEvent e)
  {
    int javaButton = e.getButton();
    int realButton;
    String description;
    if (javaButton == 1)
    {
      realButton = 0;
      description = "Left Button";
    }
    else if (javaButton == 3)
    {
      realButton = 1;
      description = "Right Button";
    }
    else if (javaButton == 2)
    {
      realButton = javaButton;
      description = "Middle Button";
    }
    else
    {
      realButton = javaButton;
      description = realButton + "th Button";
    }

    return new Button(realButton, 1, description, false, (char) 0);
  }

  private PointerLocation getPointerLocation(MouseEvent e)
  {
    int x = e.getX();
    int y = e.getY();
    int sx = e.getXOnScreen();
    int sy = e.getXOnScreen();

    return new PointerLocation(x, y, sx, sy);
  }

  @Override
  public void windowClosing(WindowEvent e)
  {
    System.exit(0);
  }

  @Override
  public void mouseMoved(MouseEvent e)
  {
    PointerLocation location = getPointerLocation(e);
    input.addInput(new PointerInput(System.nanoTime(), location));
    input.setPointerLocation(location);
  }

  public void mouseDragged(MouseEvent e)
  {
    PointerLocation location = getPointerLocation(e);
    input.addInput(new PointerInput(System.nanoTime(), location));
    input.setPointerLocation(location);
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e)
  {
    input.addInput(new WheelInput(System.nanoTime(), (float) e.getPreciseWheelRotation()));
  }

  @Override
  public void keyPressed(KeyEvent e)
  {
    int keyCode = e.getKeyCode();
    boolean keyPressed = input.getKeyState(keyCode);
    if (!keyPressed)
    {
      KeyInput keyInput = new KeyInput(System.nanoTime(), true, getKeyButton(e));

      input.addInput(keyInput);
      input.setKeyState(keyCode, true);
    }
  }

  @Override
  public void keyReleased(KeyEvent e)
  {
    int keyCode = e.getKeyCode();
    boolean keyPressed = input.getKeyState(keyCode);
    if (keyPressed)
    {
      KeyInput keyInput = new KeyInput(System.nanoTime(), false, getKeyButton(e));

      input.addInput(keyInput);
      input.setKeyState(e.getKeyCode(), false);
    }
  }

  @Override
  public void mousePressed(MouseEvent e)
  {
    PointerLocation location = getPointerLocation(e);
    MouseInput mouseInput = new MouseInput(System.nanoTime(), true, getMouseButton(e), location);

    input.addInput(mouseInput);
    input.setMouseButtonState(mouseInput.getButton(), true);
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
    PointerLocation location = getPointerLocation(e);
    MouseInput mouseInput = new MouseInput(System.nanoTime(), false, getMouseButton(e), location);

    input.addInput(mouseInput);
    input.setMouseButtonState(mouseInput.getButton(), false);
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

