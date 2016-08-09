package net.engine.game;

import java.util.LinkedHashMap;
import java.util.Map;

public class StageManager
{
  protected Stage currentStage;
  protected Stage nextStage;

  protected Map<String, Stage> stages;

  public StageManager()
  {
    currentStage = null;
    nextStage = null;
    stages = new LinkedHashMap<>();
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

  public void addStage(String name, Stage stage)
  {
    stages.put(name, stage);
  }

  public void setStage(String name)
  {
    Stage stage = stages.get(name);
    setStage(stage);
  }
}

