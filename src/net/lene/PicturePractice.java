package net.lene;

import net.engine.input.GameInput;

import java.util.Random;

/**
 * Copyright (c) Lautus Solutions.
 */
public class PicturePractice
    extends ExerciseStage
{
  private Random random;

  public PicturePractice(int renderWidth, int renderHeight)
  {
    super(renderWidth, renderHeight);
    random = new Random();
  }

  @Override
  public void tick(double time, GameInput input, int width, int height)
  {
    picture.clear(0);
    for (int i = 0; i < 1000; i++)
    {
      picture.setPixel(random.nextInt(width), random.nextInt(height), 1);
    }

    picture.circle(160, 120, 7, 7);

    picture.line(10, 10, 200, 150, 2);
    picture.line(75, 220, 270, 50, 3);
    picture.line(140, 60, 180, 230, 4);


    sleep(128);
  }
}

