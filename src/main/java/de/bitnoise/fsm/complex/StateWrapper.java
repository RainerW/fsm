package de.bitnoise.fsm.complex;

public class StateWrapper
{
  State _state;

  public State getState()
  {
    return _state;
  }

  public StateWrapper(State state) {
    _state = state;
  }
}
