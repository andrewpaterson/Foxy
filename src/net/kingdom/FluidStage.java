package net.kingdom;

import net.engine.game.Stage;
import net.engine.game.StageManager;
import net.engine.input.*;
import net.engine.picture.ColourGradient;
import net.engine.thread.Job;
import net.engine.thread.Threadanator;
import net.kingdom.fluid.FluidField;
import net.kingdom.fluid.work.FluidDrawWork;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class FluidStage extends Stage
{
  protected float force;
  protected float clickDensity;
  protected int width;
  protected int height;
  protected FluidField fluidField;

  protected boolean mLeftPressed = false;
  protected boolean mRightPressed = false;
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
    this.fluidField = new FluidField(width, height, timeStep, 0.00005f, 0.00001f, iterations, iterations);
    this.fluidFieldJob = createColourJob(fluidField, height);
  }

  void drawDensity(Graphics graphics, int windowWidth, int windowHeight, FluidField fluidField)
  {
    calculateDensity();

    convertImageRaster(fluidField.getWidth() + 2, fluidField.getHeight() + 2, pixels, bufferedImage);
    graphics.drawImage(bufferedImage, 0, 0, windowWidth, windowHeight, 0, 0, fluidField.getWidth() + 1, fluidField.getHeight() + 1, null);
  }

  private void calculateDensity()
  {
    Threadanator.getInstance().processJob(fluidFieldJob);
  }

  private Job createColourJob(FluidField fluidField, int fieldHeight)
  {
    Color[] palette = new Color[256];
    ColourGradient.generate(palette,
            new Color(0, 0, 0), 0,
            new Color(99, 44, 255), 50,
            new Color(237, 0, 56), 100,
            new Color(237, 0, 201), 150,
            new Color(237, 100, 201), 200,
            new Color(255, 253, 75), 240,
            new Color(255, 255, 255), 255);


    Job job = new Job(16);
    for (int y = 0; y <= fieldHeight; y++)
    {
      job.add(new FluidDrawWork(this, fluidField, y, palette));
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
  public void tick(double time, GameInput gameInput)
  {
    tickTime = fluidField.tick();

    gameInput.processEvents(this);

    PointerLocation mouseLocation = gameInput.getPointerLocation();
    if (mouseLocation != null)
    {
      oldMouseX = mouseX;
      oldMouseY = mouseY;
      mouseX = mouseLocation.getX();
      mouseY = mouseLocation.getY();
    }
  }

  public int[] getPixels()
  {
    return pixels;
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
}

