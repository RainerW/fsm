package de.bitnoise.tools.em;

public interface EMErrorHandler
{
  void handleTransitionError(EMState state, EMEvent event, String msg);
}
