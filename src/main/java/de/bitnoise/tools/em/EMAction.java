package de.bitnoise.tools.em;

public interface EMAction<S extends EMState, E extends EMEvent>
{
  void doAction(S currentState, E eventObj);
}
