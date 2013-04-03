package de.bitnoise.tools.em.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.bitnoise.tools.em.EMAction;

public class FsmState
{
  String _name;

  Map<String, FsmTransition> _transitions = new HashMap<String, FsmTransition>();

  List<EMAction> _onStateActions = new ArrayList<EMAction>();

  public List<EMAction> getOnStateActions()
  {
    return _onStateActions;
  }

  public FsmState(String stateName)
  {
    _name = stateName;
  }

  public String getName()
  {
    return _name;
  }
  
  public void addActions(EMAction[] actions)
  {
    _onStateActions.addAll(Arrays.asList(actions));
  }


  public FsmState addTransition(String nextState, String[] events, EMAction[] actions)
  {
    if (events == null || events.length == 0)
    {
      throw new IllegalArgumentException("events annot be null or empty");
    }
    for (String event : events)
    {
      addTransition(nextState, event, actions);
    }
    return this;
  }

  public FsmState addTransition(String nextState, String event, EMAction... actions)
  {
    FsmTransition transition = _transitions.get(event);
    if (transition != null)
    {
      if (!transition.getNextState().equals(nextState))
      {
        throw new IllegalStateException("the existring transision had the state " + transition.getNextState()
            + " expected to be " + nextState);
      }
    }
    else
    {
      transition = new FsmTransition(nextState);
      _transitions.put(event, transition);
    }
    if (actions != null)
    {
      transition.addActions(actions);
    }
    return this;
  }
  
  public FsmTransition getTransitionFor(String eventName)
  {
    return _transitions.get(eventName);
  }

}
