package de.bitnoise.tools.em.impl;

import de.bitnoise.tools.em.EMEvent;
import de.bitnoise.tools.em.EMState;

public class EMEventImpl implements EMEvent<EMState>
{
  String _eventName;

  public EMEventImpl(String eventName)
  {
    _eventName = eventName;
  }

  @Override
  public String getEventName()
  {
    return _eventName;
  }

}
