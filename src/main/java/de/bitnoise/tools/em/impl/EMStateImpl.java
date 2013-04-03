package de.bitnoise.tools.em.impl;

import de.bitnoise.tools.em.EMState;

public class EMStateImpl implements EMState
{
  String _name;

  public EMStateImpl(String stateName)
  {
    _name = stateName;
  }

  @Override
  public String getStateName()
  {
    return _name;
  }

}
