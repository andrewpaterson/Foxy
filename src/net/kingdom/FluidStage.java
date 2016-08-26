package net.kingdom;

import net.engine.game.Stage;
import net.engine.game.StageManager;
import net.engine.input.GameInput;
import net.engine.thread.Job;
import net.engine.thread.Threadanator;
import net.kingdom.fluid.FluidField;
import net.kingdom.fluid.work.FluidDrawWork;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class FluidStage extends Stage
{
  protected float force;
  protected float clickDensity;
  protected int width;
  protected int height;
  protected FluidField fluidField;

  protected boolean mLeftPressed = false;
  protected boolean mRightPressed = true;
  protected int mouseX;
  protected int mouseY;
  protected int oldMouseX;
  protected int oldMouseY;

  protected BufferedImage bufferedImage = null;
  protected int[] pixels = null;
  protected double tickTime;
  protected Job fluidFieldJob;

  public FluidStage(float force, float clickDensity, int width, int height, int iterations, float timeStep)
  {
    this.force = force;
    this.clickDensity = clickDensity;
    this.width = width;
    this.height = height;
    this.fluidField = new FluidField(width, height, timeStep, 0, 0, iterations, iterations);
    this.fluidFieldJob = createColourJob(fluidField, width, height);
  }

  void drawDensity(Graphics graphics, int windowWidth, int windowHeight, FluidField fluidField)
  {
    calculateDensity();

    convertIntsToImageRaster(fluidField.getWidth() + 2, fluidField.getHeight() + 2, pixels, bufferedImage);
    graphics.drawImage(bufferedImage, 0, 0, windowWidth, windowHeight, 0, 0, fluidField.getWidth() + 1, fluidField.getHeight() + 1, null);
  }

  private void calculateDensity()
  {
    Threadanator.getInstance().processJob(fluidFieldJob);
  }

  private Job createColourJob(FluidField fluidField, int fieldWidth, int fieldHeight)
  {
    Job job = new Job(16);
    for (int y = 0; y <= fieldHeight; y++)
    {
      job.add(new FluidDrawWork(this, fluidField, y));
    }
    return job;
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

    int i = (int) ((mouseX / (float) bufferWidth) * fieldWidth + 1);
    int j = (int) ((mouseY / (float) bufferHeight) * fieldHeight + 1);

    if ((i < 1) || (i > fieldWidth)
            || (j < 1) || (j > fieldHeight))
    {
      return;
    }

    if (mLeftPressed)
    {
      fluidField.setForce(i, j, force * (mouseX - oldMouseX), -force * (oldMouseY - mouseY));
    }

    if (mRightPressed)
    {
      fluidField.setDensity(i, j, clickDensity);
    }

    oldMouseX = mouseX;
    oldMouseY = mouseY;
  }

  void mousePressed(GameInput input)
  {
    Point mouseLocation = input.getMouseLocation();
    if (mouseLocation != null)
    {
      oldMouseX = mouseLocation.x;
      mouseX = oldMouseX;
      oldMouseY = mouseLocation.y;
      mouseY = oldMouseY;
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

  @Override
  public void stageStarting(StageManager stageManager)
  {
    super.stageStarting(stageManager);
    bufferedImage = new BufferedImage(width + 2, height + 2, BufferedImage.TYPE_INT_ARGB);
    pixels = new int[(width + 2) * (height + 2)];
  }

  @Override
  public void render(Graphics graphics, int width, int height)
  {
    setForces(width, height, fluidField);

    drawDensity(graphics, width, height, fluidField);

    String s = String.format("%.3fms", tickTime);
    graphics.drawChars(s.toCharArray(), 0, s.length(), 15, 15);
  }

  @Override
  public void tick(double time, GameInput input)
  {
    tickTime = fluidField.tick();

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
      mouseX = mouseLocation.x;
      mouseY = mouseLocation.y;
    }
  }

  public int[] getPixels()
  {
    return pixels;
  }
}

