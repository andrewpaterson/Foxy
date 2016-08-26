package net.engine.thread;

/**
 * Created by andrew on 2016/08/26.
 */
public class NotPreparedException extends RuntimeException
{
  public NotPreparedException()
  {
    super("Cannot process when not Prepared.");
  }
}

