package de.bitnoise.fsm.simple;

public class Transition
{

  String fromState;

  String onEvent;

  String nextState;

  FsmAction doAction;

  public Transition(String state, String event, String next, FsmAction action)
  {
    fromState = state;
    onEvent = event;
    nextState = next;
    doAction = action;
  }

  public String getFromState()
  {
    return fromState;
  }

  public String getOnEvent()
  {
    return onEvent;
  }

  public String getNextState()
  {
    return nextState;
  }

  public FsmAction getDoAction()
  {
    return doAction;
  }

}
