package net.kingdom;

import net.engine.game.PictureStage;
import net.engine.game.StageManager;
import net.engine.input.GameInput;
import net.engine.input.KeyInput;
import net.engine.input.MouseInput;
import net.engine.input.PointerInput;
import net.engine.thread.Job;
import net.engine.thread.Threadanator;
import net.kingdom.fluid.FluidField;
import net.kingdom.fluid.work.FluidDrawWork;

import java.awt.*;
import java.awt.event.KeyEvent;

public class FluidStage extends PictureStage
{
  protected float force;
  protected float clickDensity;
  protected int width;
  protected int height;
  protected FluidField fluidField;

  protected boolean mLeftPressed;
  protected boolean mRightPressed;
  protected int mouseX;
  protected int mouseY;
  protected int oldMouseX;
  protected int oldMouseY;

  protected double tickTime;
  protected Job fluidFieldJob;

  public FluidStage(float force, float clickDensity, int iterations, float timeStep, int renderWidth, int renderHeight, int windowWidth, int windowHeight)
  {
    super(renderWidth + 2, renderHeight + 2, windowWidth, windowHeight);
    this.force = force;
    this.clickDensity = clickDensity;
    this.width = renderWidth;
    this.height = renderHeight;
    this.fluidField = new FluidField(width, height, timeStep, 0.00005f, 0.00001f, iterations, iterations);
    this.fluidFieldJob = createColourJob(fluidField, height);
    frameBuffer.setPaletteFromColourGradient(
            new Color(0, 0, 0), 0,
            new Color(99, 44, 255), 50,
            new Color(237, 0, 56), 100,
            new Color(237, 0, 201), 150,
            new Color(237, 100, 201), 200,
            new Color(255, 253, 75), 240,
            new Color(255, 255, 255), 255);
  }

  void drawDensity(Graphics graphics, int windowWidth, int windowHeight)
  {
    calculateDensity();

    renderPictureToWindow(graphics, windowWidth, windowHeight);
  }

  private void calculateDensity()
  {
    Threadanator.getInstance().process(fluidFieldJob);
  }

  private Job createColourJob(FluidField fluidField, int fieldHeight)
  {
    Job job = new Job(8);
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
  }

  @Override
  public void render(Graphics graphics, int width, int height)
  {
    setForces(width, height, fluidField);

    drawDensity(graphics, width, height);

    String s = String.format("%.3fms", tickTime);
    graphics.drawChars(s.toCharArray(), 0, s.length(), 15, 15);
  }

  @Override
  public void tick(double time, GameInput gameInput)
  {
    tickTime = fluidField.tick();

    oldMouseX = mouseX;
    oldMouseY = mouseY;
    gameInput.processEvents(this);
  }

  public byte[] getPixels()
  {
    return frameBuffer.getPixels();
  }

  @Override
  public void mouseInput(MouseInput input)
  {
    if (input.getButton() == 0)
    {
      mLeftPressed = input.isPressed();
    }
    if (input.getButton() == 1)
    {
      mRightPressed = input.isPressed();
    }
  }

  @Override
  public void keyInput(KeyInput input)
  {
    if (input.getKey() == KeyEvent.VK_C)
    {
      fluidField.clearData();
    }
  }

  @Override
  public void pointerInput(PointerInput input)
  {
    mouseX = input.getX();
    mouseY = input.getY();
  }
}

