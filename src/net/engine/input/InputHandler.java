package net.engine.input;

public interface InputHandler
{
  void mouseInput(MouseInput input);

  void keyInput(KeyInput input);

  void pointerInput(PointerInput input);

  void wheelInput(WheelInput input);
}

