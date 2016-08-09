package net.engine.common;

public class StageManager
{
  protected Stage currentStage;
  protected Stage nextStage;

  public StageManager()
  {
    currentStage = null;
    nextStage = null;
  }

  public void setStage(Stage stage)
  {
    nextStage = stage;
  }

  public Stage getCurrentStage()
  {
    return currentStage;
  }

  public Stage tickStage()
  {
    if (nextStage != null)
    {
      currentStage = nextStage;
      nextStage.stageEnding();
      nextStage = null;
      currentStage.stageStarting(this);
    }

    return currentStage;
  }
}

