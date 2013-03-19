package de.bitnoise.fsm.complex;

import de.bitnoise.fsm.simple.FsmEvent;

public class Event<T extends Object> implements FsmEvent
{
  String _eventName;

  T _payload;

  public Event(String eventName)
  {
    _eventName = eventName;
  }

  public Event(String eventName, T plyload)
  {
    _eventName = eventName;
    _payload = plyload;
  }

  public String getEventName()
  {
    return _eventName;
  }

  public T getEventObject()
  {
    return _payload;
  }

  @Override
  public String toString()
  {
    return _eventName;
  }

}
