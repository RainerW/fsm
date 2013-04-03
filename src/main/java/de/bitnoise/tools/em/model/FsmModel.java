package de.bitnoise.tools.em.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.bitnoise.tools.em.EMAction;

public class FsmModel
{
  Map<String, FsmState> states = new HashMap<String, FsmState>();

  List<EMAction> _onStateListeners = new ArrayList<EMAction>();

  String _startStateName;

  public FsmState addStartState(String stateName, EMAction... actions)
  {
    _startStateName = stateName;
    return addState(stateName, actions);
  }
  
  public FsmState addState(String stateName, EMAction... actions)
  {
    FsmState state = states.get(stateName);
    if (state == null)
    {
      state = new FsmState(stateName);
      states.put(stateName, state);
    }
    if (actions != null)
    {
      state.addActions(actions);
    }
    return state;
  }

  public FsmState getState(String stateName)
  {
    return states.get(stateName);
  }

  public void setOnStateListeners(EMAction... actions)
  {
    if (actions == null)
    {
      _onStateListeners = new ArrayList<EMAction>();
    }
    else
    {
      _onStateListeners = Arrays.asList(actions);
    }
  }

  public List<EMAction> getOnStateListeners()
  {
    return _onStateListeners;
  }

  public String getStartStateName()
  {
    return _startStateName;
  }

}
