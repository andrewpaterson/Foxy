package net.engine.scene;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 2016/08/09.
 */
public class Scene
{
  protected List<Sprite> sprites;
  protected Camera camera;

  public Scene()
  {
    sprites = new ArrayList<>();
    camera = new Camera();
  }

  public Camera getCamera()
  {
    return camera;
  }

  public void addSprite(Sprite sprite)
  {
    sprites.add(sprite);
  }

  public void removeSprite(Sprite sprite)
  {
    sprites.remove(sprite);
  }

  public void render(Graphics graphics, int width, int height)
  {
    List[] layers;
    layers = new List[50];
    for (int i = 0; i < layers.length; i++)
    {
      layers[i] = new ArrayList<Sprite>();
    }

    for (Sprite sprite : sprites)
    {
      if (sprite.isVisible())
      {
        int layer = sprite.layer;
        if (layer < 50)
        {
          layers[layer].add(sprite);
        }
      }
    }

    for (int i = layers.length - 1; i >= 0; i--)
    {
      for (Object o : layers[i])
      {
        Sprite sprite = (Sprite) o;
        sprite.render(graphics, camera);
      }
    }
  }
}

