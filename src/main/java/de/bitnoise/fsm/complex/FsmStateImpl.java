package de.bitnoise.fsm.complex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.bitnoise.fsm.simple.FsmAction;

public class FsmStateImpl implements State
{
  String _name;

  ArrayList<FsmAction> _actions;

  public FsmStateImpl(String name)
  {
    _name = name;
  }

  @Override
  public String toString()
  {
    return _name;
  }

  @Override
  public String getSymbolicStateId()
  {
    return _name;
  }

  public void setOnStateActions(FsmAction[] onStateActions)
  {
    _actions = new ArrayList<FsmAction>();
    if (onStateActions != null)
    {
      _actions.addAll(Arrays.asList(onStateActions));
    }
  }

  @Override
  public List<FsmAction> getOnStateActions()
  {
    return _actions;
  }

}
