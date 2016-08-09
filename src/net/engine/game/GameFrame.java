package net.engine.game;

import net.engine.input.GameInput;

import javax.swing.*;
import java.awt.*;

public class GameFrame
        extends JFrame
{
  private GameCanvas canvas;
  private GameInput input;

  public GameFrame(String title) throws HeadlessException
  {
    super(title);

    canvas = new GameCanvas();
    input = null;

    add(canvas, BorderLayout.CENTER);
    setSize(600, 400);

  }

  @Override
  public void setVisible(boolean b)
  {
    super.setVisible(b);
  }

  public GameCanvas getCanvas()
  {
    return canvas;
  }

  public void start()
  {
    input = new GameInput();
    new InputAdapter(this, canvas, input);

    canvas.start();
  }

  public GameInput processInput()
  {
    input.setMouseLocation();
    return input;
  }
}

