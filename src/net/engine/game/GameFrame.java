package net.engine.game;

import net.engine.global.GlobalGraphics;
import net.engine.input.GameInput;
import net.engine.input.InputAdapter;

import javax.swing.*;
import java.awt.*;

public class GameFrame
        extends JFrame
{
  private GameCanvas canvas;
  private GameInput input;

  public GameFrame(FrameConfig config) throws HeadlessException
  {
    super(config.title);

    canvas = new GameCanvas();
    input = null;

    add(canvas, BorderLayout.CENTER);

    setSize(config.width, config.height);
    setLocation(config.left, config.top);
    setAlwaysOnTop(config.onTop);
    setUndecorated(!config.decorated);
    setFullScreen(config.fullScreen);
  }

  private void setFullScreen(boolean fullScreen)
  {
    if (fullScreen)
    {
      GlobalGraphics.fullScreen(this, getWidth(), getHeight());
    }
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
    return input;
  }
}

