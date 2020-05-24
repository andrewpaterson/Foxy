package net.engine.input;

public interface InputHandler
{
  void mouseInput(MouseInput input, GameInput gameInput, int width, int height);

  void keyInput(KeyInput input, GameInput gameInput, int width, int height);

  void pointerInput(PointerInput input, GameInput gameInput, int width, int height);

  void wheelInput(WheelInput input, GameInput gameInput, int width, int height);
}

