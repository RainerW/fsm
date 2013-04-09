package de.bitnoise.tools.em.impl;

import de.bitnoise.tools.em.EMEvent;

public class EMEventImpl implements EMEvent
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
