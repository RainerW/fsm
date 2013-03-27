package de.bitnoise.fsm.simple;

public interface FsmEvent<T>
{
  String getEventName();

  T getEventObject();
}
