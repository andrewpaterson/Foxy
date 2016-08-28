package net.kingdom;

import net.engine.game.PictureStage;
import net.engine.input.GameInput;
import net.engine.input.KeyInput;
import net.engine.input.PointerInput;
import net.engine.picture.BasePicture;
import net.engine.picture.PalettePicture;
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

  protected int mouseX;
  protected int mouseY;
  protected int oldMouseX;
  protected int oldMouseY;

  protected double tickTime;
  protected Job fluidFieldJob;

  public FluidStage(float force, float clickDensity, int iterations, float timeStep, int renderWidth, int renderHeight)
  {
    super(new PalettePicture(renderWidth, renderHeight));
    this.force = force;
    this.clickDensity = clickDensity;
    this.width = renderWidth;
    this.height = renderHeight;
    this.fluidField = new FluidField(width, height, timeStep, 0.00005f, 0.00001f, iterations, iterations);
    this.fluidFieldJob = createColourJob(fluidField, height);
    this.picture.setPaletteFromColourGradient(
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
    drawDensity();
    picture.speckle(1);

    renderPictureToWindow(graphics, windowWidth, windowHeight);
  }

  private void drawDensity()
  {
    Threadanator.getInstance().process(fluidFieldJob);
  }

  private Job createColourJob(FluidField fluidField, int fieldHeight)
  {
    Job job = new Job(8);
    for (int y = 1; y < fieldHeight; y++)
    {
      job.add(new FluidDrawWork(this, fluidField, y));
    }
    return job;
  }

  void setForces(GameInput gameInput, int width, int height)
  {
    int fieldWidth = fluidField.getWidth();
    int fieldHeight = fluidField.getHeight();

    fluidField.clearPrevious();

    if (!(gameInput.getMouseButtonState(0) || gameInput.getMouseButtonState(1)))
    {
      return;
    }

    int i = (int) (mouseX * widthScale(width));
    int j = (int) (mouseY * heightScale(height));

    if ((i < 1) || (i > fieldWidth)
            || (j < 1) || (j > fieldHeight))
    {
      return;
    }

    if (gameInput.getMouseButtonState(0))
    {
      fluidField.setForce(i, j, force * (mouseX - oldMouseX), -force * (oldMouseY - mouseY));
    }

    if (gameInput.getMouseButtonState(1))
    {
      fluidField.setDensity(i, j, clickDensity);
    }
  }

  @Override
  public void render(Graphics graphics, int width, int height)
  {
    drawDensity(graphics, width, height);

    String s = String.format("%.3fms", tickTime);
    graphics.drawChars(s.toCharArray(), 0, s.length(), 15, 15);
  }

  @Override
  public void tick(double time, GameInput gameInput, int width, int height)
  {
    tickTime = fluidField.tick();

    oldMouseX = mouseX;
    oldMouseY = mouseY;
    gameInput.processEvents(this, width, height);

    setForces(gameInput, width, height);
  }

  public BasePicture getPicture()
  {
    return picture;
  }

  @Override
  public void keyInput(KeyInput input, GameInput gameInput, int width, int height)
  {
    if (input.getKey() == KeyEvent.VK_C)
    {
      fluidField.clearData();
    }
  }

  @Override
  public void pointerInput(PointerInput input, GameInput gameInput, int width, int height)
  {
    mouseX = input.getX();
    mouseY = input.getY();
  }
}

