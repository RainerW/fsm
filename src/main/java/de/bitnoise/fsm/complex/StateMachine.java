package de.bitnoise.fsm.complex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.bitnoise.fsm.simple.FsmAction;
import de.bitnoise.fsm.simple.FsmEvent;

public class StateMachine
{

  Map<State, Map<String, NextState>> transitions;
  Map<String, State> trans2;

  public StateMachine(Map<State, Map<String, NextState>> table)
  {
    transitions = table;
    trans2 = new HashMap<String, State>();
    for (Entry<State, Map<String, NextState>> entry : transitions.entrySet())
    {
      State state = entry.getKey();
      String name = state.getSymbolicStateId();
      trans2.put(name, state);
    }
  }

  public State event(String stateName, String event)
  {
    return event(trans2.get(stateName), event);
  }
  
  public State event(String stateName, FsmEvent  event)
  {
    return event(trans2.get(stateName), event);
  }

  public State event(State currentState, final String event)
  {
    return event(currentState, new Event<Object>(event));
  }

  public State event(State currentState, FsmEvent eventObj)
  {
    System.out.println("from State : " + currentState);
    System.out.println("  on Event : " + eventObj);
    String event = eventObj.getEventName();
    Map<String, NextState> state = transitions.get(currentState);
    if (state == null)
    {
      noTransitionsFound(currentState, event);
    }
    NextState transition = state.get(event);
    if (transition == null)
    {
      noTransitionFound(currentState, event);
    }
    List<FsmAction> actions = transition.getDoAction();
    for (FsmAction action : actions)
    {
      action.doAction(eventObj);
    }
    State nextState = transition.getNextState();
    System.out.println("  to State : " + nextState);
    return nextState;
  }

  /**
   * There does not exist an transitions for the given event
   */
  protected void noTransitionFound(State currentState, String event)
  {
    throw new IllegalStateException("There are no transitions for the event :" + event + " in the state :"
        + currentState);
  }

  /**
   * there are no transtions for the current State at all
   */
  protected void noTransitionsFound(State currentState, String event)
  {
    throw new IllegalStateException("There are no transitions from the State '" + currentState
        + "' on event :" + event);
  }

}
