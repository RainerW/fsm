package de.bitnoise.tools.em.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.bitnoise.tools.em.EMAction;

public class FsmTransition
{
  String _nextState;

  List<EMAction> _actions = new ArrayList<EMAction>();

  public FsmTransition(String nextState)
  {
    _nextState = nextState;
  }

  public String getNextState()
  {
    return _nextState;
  }

  public void addActions(EMAction[] actions)
  {
    _actions.addAll(Arrays.asList(actions));
  }

  public List<EMAction> getActions()
  {
    return _actions;
  }

}
