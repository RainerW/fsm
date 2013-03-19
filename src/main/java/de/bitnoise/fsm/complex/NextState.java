package de.bitnoise.fsm.complex;

import java.util.ArrayList;
import java.util.List;

import de.bitnoise.fsm.simple.FsmAction;

public class NextState
{
  State _nextState;
  List<FsmAction> _actions = new ArrayList<FsmAction>();

  public NextState(State nextState)
  {
    _nextState = nextState;
  }

  public void addActions(FsmAction[] doActions)
  {
    if (doActions != null)
    {
      for (FsmAction action : doActions)
      {
        _actions.add(action);
      }
    }
  }

  public List<FsmAction> getDoAction()
  {
    return _actions;
  }

  public State getNextState()
  {
    return _nextState;
  }

}
