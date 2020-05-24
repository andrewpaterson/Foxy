package net.lene;

import net.engine.input.GameInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Copyright (c) Lautus Solutions.
 */
public class PicturePractice
    extends ExerciseStage
{
  private List<Bar> bars;
  private Random random;

  public PicturePractice(int renderWidth, int renderHeight)
  {
    super(renderWidth, renderHeight);
    random = new Random();
    bars = new ArrayList<>();

    int[][] colours = new int[4][];
    colours[0] = new int[]{11, 5, 13, 1, 5, 13, 11};
    colours[1] = new int[]{11, 8, 7, 1, 7, 8, 11};
    colours[2] = new int[]{11, 6, 14, 1, 14, 6, 11};
    colours[3] = new int[]{11, 2, 10, 1, 2, 10, 11};

    for (int i = 0; i < 20; i++)
    {
      int mod = i % 4;

      if (random.nextBoolean())
      {
        bars.add(new HorizontalBar(random.nextInt(150) + 50, colours[mod]));
      }
      else
      {
        bars.add(new VerticalBar(random.nextInt(300) - 150, colours[mod]));
      }
    }
  }

  public void tick(double time, GameInput input, int width, int height)
  {
    picture.clear(0);

    for (Bar bar : bars)
    {
      bar.tick(time, picture);
    }
  }
}

