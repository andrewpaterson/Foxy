package net.kingdom;

import net.engine.game.Stage;
import net.engine.game.StageManager;
import net.engine.global.GlobalGraphics;
import net.engine.input.GameInput;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class FluidStage extends Stage
{
  protected float force = 5.0f;
  protected float source = 100.0f;
  protected FluidField fluidField;

  //For ui
  protected boolean mLeftPressed = false;
  protected boolean mRightPressed = true;
  protected int mx;
  protected int my; //Current mouse position
  protected int omx;
  protected int omy; //Old mouse position
  protected BufferedImage bufferedImage = null;
  protected int[] pixels = null;

  void drawDensity(Graphics graphics, int bufferWidth, int bufferHeight, FluidField fluidField)
  {
    int i, j;
    float d00, d01, d10, d11;

    int fieldWidth = fluidField.getWidth();
    int fieldHeight = fluidField.getHeight();
    for (i = 0; i <= fieldWidth; i++)
    {
      for (j = 0; j <= fieldHeight; j++)
      {
        fluidField.getDensity(i, j);
        d00 = fluidField.getDensity(i, j);
        d01 = fluidField.getDensity(i, j + 1);
        d10 = fluidField.getDensity(i + 1, j);
        d11 = fluidField.getDensity(i + 1, j + 1);

        pixels[(i + ((fieldWidth + 2) * j))] = getColour((d00 + d01 + d10 + d11) / 4);
      }
    }
    GlobalGraphics.convertFromIntArray(fieldWidth + 2, fieldHeight + 2, pixels, bufferedImage);
    graphics.drawImage(bufferedImage, 0, 0, bufferWidth, bufferHeight, 0, 0, fieldWidth + 1, fieldHeight + 1, null);
  }

  private int getColour(float colour)
  {
    if (colour < 0)
    {
      colour = 0;
    }
    if (colour > 1)
    {
      colour = 1;
    }
    int bits = (int) (colour * 255);
    return bits | bits << 8 | bits << 16 | 0xff << 24;
  }

  void setForces(int bufferWidth, int bufferHeight, FluidField fluidField)
  {
    int fieldWidth = fluidField.getWidth();
    int fieldHeight = fluidField.getHeight();

    fluidField.clearPrevious();

    if (!(mLeftPressed || mRightPressed))
    {
      return;
    }

    int i = (int) ((mx / (float) bufferWidth) * fieldWidth + 1);
    int j = (int) ((my / (float) bufferHeight) * fieldHeight + 1);

    if ((i < 1) || (i > fieldWidth)
            || (j < 1) || (j > fieldHeight))
    {
      return;
    }

    if (mLeftPressed)
    {
      fluidField.setForce(i, j, force * (mx - omx), -force * (omy - my));
    }

    if (mRightPressed)
    {
      fluidField.setDensity(i, j, source);
    }

    omx = mx;
    omy = my;
  }

  void mousePressed(GameInput input)
  {
    Point mouseLocation = input.getMouseLocation();
    if (mouseLocation != null)
    {
      omx = mouseLocation.x;
      mx = omx;
      omy = mouseLocation.y;
      my = omy;
      mLeftPressed = input.getMouseButtonState(0);
      mRightPressed = input.getMouseButtonState(1);
    }
  }

  void keyPressed(GameInput input, FluidField fluidField)
  {
    if (input.getKeyState(KeyEvent.VK_C))
    {
      fluidField.clearData();
    }
  }

  void draw(Graphics graphics, int width, int height)
  {
    setForces(width, height, fluidField);

    graphics.setColor(Color.BLACK);
    graphics.fillRect(0, 0, width, height);

    drawDensity(graphics, width, height, fluidField);
  }

  @Override
  public void stageStarting(StageManager stageManager)
  {
    super.stageStarting(stageManager);
    int size = 340;
    fluidField = new FluidField(size, 0.4f, 0, 0);
    bufferedImage = new BufferedImage(size + 2, size + 2, BufferedImage.TYPE_INT_ARGB);
    pixels = new int[(size + 2) * (size + 2)];
  }

  @Override
  public void stageEnding()
  {
    super.stageEnding();
  }

  @Override
  public void render(Graphics graphics, int width, int height)
  {
    draw(graphics, width, height);
  }

  @Override
  public void tick(double time, GameInput input)
  {
    fluidField.tick();

    List<InputEvent> inputEvents = input.popEvents();
    for (InputEvent inputEvent : inputEvents)
    {
      if (inputEvent instanceof MouseEvent)
      {
        mousePressed(input);
      }
      if (inputEvent instanceof KeyEvent)
      {
        keyPressed(input, fluidField);
      }
    }

    Point mouseLocation = input.getMouseLocation();
    if (mouseLocation != null)
    {
      mx = mouseLocation.x;
      my = mouseLocation.y;
    }
  }
}

