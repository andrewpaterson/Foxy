package net.engine.common.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by andrew on 2016/08/09.
 */
public class PaintEvent implements ActionListener
{
  protected GameCanvas gameCanvas;

  public PaintEvent(GameCanvas gameCanvas)
  {
    this.gameCanvas = gameCanvas;
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    gameCanvas.render();
  }
}

