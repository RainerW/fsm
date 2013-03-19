package de.bitnoise.fsm.simple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StateMachine
{
  Map<String, Map<String, Transition>> transitions;

  public StateMachine(Object[][] fsm)
  {
    Map<String, List<Transition>> temp = new HashMap<String, List<Transition>>();
    for (Object[] row : fsm)
    {
      String state = (String) row[0];
      String event = (String) row[1];
      String next = (String) row[2];
      FsmAction action = (FsmAction) row[3];
      List<Transition> list = temp.get(state);
      if (list == null)
      {
        list = new ArrayList<Transition>();
        temp.put(state, list);
      }
      list.add(new Transition(state, event, next, action));
    }
    transitions = new HashMap<String, Map<String, Transition>>();
    for (Entry<String, List<Transition>> state : temp.entrySet())
    {
      String name = state.getKey();
      Map<String, Transition> eventMap = new HashMap<String, Transition>();
      transitions.put(name, eventMap);
      for (Transition transition : state.getValue())
      {
        String event = transition.getOnEvent();
        eventMap.put(event, transition);
      }
    }
  }

  public String event(String currentState, final String event)
  {
    return event(currentState, new Event<Object>(event));
  }

  public String event(String currentState, FsmEvent eventObj)
  {
    String event = eventObj.getEventName();
    Map<String, Transition> eventMap = transitions.get(currentState);
    if (eventMap == null)
    {
      noTransitionsFound(currentState, event);
    }
    Transition transition = eventMap.get(event);
    if (transition == null)
    {
      noTransitionFound(currentState, event);
    }
    transition.getDoAction().doAction(eventObj);
    String nextState = transition.getNextState();
    return nextState;
  }

  /**
   * There does not exist an transitions for the given event
   */
  protected void noTransitionFound(String currentState, String event)
  {
    throw new IllegalStateException("There are no transitions for the event :" + event + " in the state :"
        + currentState);
  }

  /**
   * there are no transtions for the current State at all
   */
  protected void noTransitionsFound(String currentState, String event)
  {
    throw new IllegalStateException("There are no transitions from the State '" + currentState
        + "' on event :" + event);
  }

}
