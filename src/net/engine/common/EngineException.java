package net.engine.common;

public class EngineException extends RuntimeException
{
  public EngineException()
  {
  }

  public EngineException(String unformattedMessage, Object... args)
  {
    super(String.format(unformattedMessage, args));
  }
}

