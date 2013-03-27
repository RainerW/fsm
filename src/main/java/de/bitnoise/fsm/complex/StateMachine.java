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

  State _startState;

  String GENERIC_START_EVENT = "generic-start-of-stateMachine-event";

  List<FsmAction> _globalOnStateActions;

  public StateMachine(State startState, Map<State, Map<String, NextState>> table)
  {
    _startState = startState;
    transitions = table;
    trans2 = new HashMap<String, State>();
    for (Entry<State, Map<String, NextState>> entry : transitions.entrySet())
    {
      State state = entry.getKey();
      String name = state.getSymbolicStateId();
      trans2.put(name, state);
    }
  }

  public String start(FsmEvent event)
  {
    if (_startState != null)
    {
      executActions(_globalOnStateActions, event);
      executActions(_startState.getOnStateActions(), event);
    }
    return _startState.toString();
  }

  public String start()
  {
    Event<Object> event = new Event<Object>(GENERIC_START_EVENT);
    return start(event);
  }

  public State event(String stateName, String event)
  {
    return event(trans2.get(stateName), event);
  }

  public State event(String stateName, FsmEvent event)
  {
    return event(trans2.get(stateName), event);
  }

  public State event(State currentState, final String event)
  {
    return event(currentState, new Event<Object>(event));
  }

  public State event(State currentState, FsmEvent eventObj)
  {
    debug("from State : " + currentState);
    debug("  on Event : " + eventObj);
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

    executActions(transition.getDoAction(), eventObj);

    State nextState = transition.getNextState();
    debug("  to State : " + nextState);
    if (!currentState.equals(nextState))
    {
      executActions(_globalOnStateActions, eventObj);
      executActions(nextState.getOnStateActions(), eventObj);
    }
    return nextState;
  }

  @Deprecated // remove this or replace by logger
  protected void debug(String msg)
  {
//    System.out.println(msg);
  }

  protected void executActions(List<FsmAction> actions, FsmEvent eventObj)
  {
    if (actions != null)
    {
      for (FsmAction action : actions)
      {
        action.doAction(eventObj);
      }
    }
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

  public void setGlobalOnStateActions(List<FsmAction> actions)
  {
    _globalOnStateActions = actions;
  }

}
