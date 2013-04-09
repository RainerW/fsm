package de.bitnoise.tools.em.impl;

import de.bitnoise.tools.em.EMState;

public class EMStateImpl implements EMState
{
  String _name;

  @Override
  public String getStateMemento()
  {
    return _name;
  }

  @Override
  public void setStateMemento(String data)
  {
    _name = data;
  }

}
