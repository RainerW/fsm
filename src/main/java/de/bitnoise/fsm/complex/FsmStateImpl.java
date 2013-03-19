package de.bitnoise.fsm.complex;

public class FsmStateImpl implements State
{
  String _name;

  public FsmStateImpl(String name)
  {
    _name = name;
  }

  @Override
  public String toString()
  {
    return _name;
  }

  @Override
  public String getSymbolicStateId()
  {
    return _name;
  }

}
