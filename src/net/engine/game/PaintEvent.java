package net.engine.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

