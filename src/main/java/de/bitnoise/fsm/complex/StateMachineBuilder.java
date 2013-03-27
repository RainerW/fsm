package de.bitnoise.fsm.complex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.bitnoise.fsm.complex.StateMachineBuilder.StateBuilder;
import de.bitnoise.fsm.simple.FsmAction;

public class StateMachineBuilder
{
  public static StateBuilder state(String name,FsmAction ... onStateActions)
  {
    return new StateBuilder(new BuilderState()).state(name,onStateActions);
  }

  public static StateBuilder state(State newState,FsmAction ... onStateActions)
  {
    return new StateBuilder(new BuilderState()).state(newState,onStateActions);
  }
  
  public static StateBuilder startState(State newState,FsmAction ... onStateActions)
  {
    return new StateBuilder(new BuilderState()).startState(newState,onStateActions);
  }
  
  public static StateBuilder startState(String name,FsmAction ... onStateActions)
  {
    return new StateBuilder(new BuilderState()).startState(name,onStateActions);
  }


  static class BuilderState
  {
    StateBuilder _stateBuilder;
    Map<String, State> states = new HashMap<String, State>();
    State _startState;
    List<FsmAction> _onEventListeners;

    public void setBuilder(StateBuilder stateBuilder)
    {
      _stateBuilder = stateBuilder;
    }

    public State findState(String name)
    {
      State s = states.get(name);
      if (s == null)
      {
        s = new FsmStateImpl(name);
        states.put(name, s);
      }
      return s;
    }

    public void setOnStateListeners(FsmAction[] listeners)
    {
      _onEventListeners=new ArrayList<FsmAction>();
      if(listeners!=null) 
      {
        _onEventListeners.addAll(Arrays.asList(listeners));
      }
    }
  }

  public static class StateBuilder
  {
    BuilderState _state;

    Map<State, Map<String, NextState>> table;
    HashMap<String, NextState> events;

    public StateBuilder(BuilderState builderState)
    {
      _state = builderState;
      _state.setBuilder(this);
      table = new HashMap<State, Map<String, NextState>>();
    }

    public StateBuilder state(String name,FsmAction ...onStateActions)
    {
      State newState = _state.findState(name);
      return state(newState,onStateActions);
    }
    
    public StateBuilder startState(String name,FsmAction ...onStateActions)
    {
      State newState = _state.findState(name);
      return startState(newState,onStateActions);
    }

    public StateBuilder state(State newState,FsmAction ...onStateActions)
    {
      events = new HashMap<String, NextState>();
      newState.setOnStateActions(onStateActions);
      table.put(newState, events);
      _state.states.put(newState.toString(), newState);
      return this;
    }
    
    public StateBuilder startState(State newState,FsmAction ...onStateActions)
    {
      _state._startState=newState;
      return state(newState,onStateActions);
    }

    public OnBuilder on(String... eventList)
    {
      return new OnBuilder(_state, events, eventList);
    }

    public StateBuilder builder()
    {
      return _state._stateBuilder;
    }

    public StateMachine createStateMachine()
    {
      StateMachine machine = new StateMachine(_state._startState,_state._stateBuilder.table);
      machine.setGlobalOnStateActions(_state._onEventListeners);
      return machine;
    }

    public StateBuilder setOnStateListeners(FsmAction ... listeners)
    {
      _state.setOnStateListeners(listeners);
      return _state._stateBuilder;
    }

  }

  public static class OnBuilder
  {
    BuilderState _state;
    HashMap<String, NextState> _events;
    String[] _eventList;

    public OnBuilder(BuilderState builderState, HashMap<String, NextState> events, String[] eventList)
    {
      _state = builderState;
      _events = events;
      _eventList = eventList;
    }

    public GoToBuilder goTo(State nextState)
    {
      NextState next = new NextState(nextState);
      for (String event : _eventList)
      {
        _events.put(event, next);
      }
      return new GoToBuilder(_state, next);
    }

    public GoToBuilder goTo(String name)
    {
      State nextState = _state.findState(name);
      return goTo(nextState);
    }
  }

  public static class GoToBuilder
  {
    BuilderState _state;
    NextState _next;

    public GoToBuilder(BuilderState builderState, NextState next)
    {
      _state = builderState;
      _next = next;
    }

    public StateBuilder state(State newState)
    {
      return _state._stateBuilder.state(newState);
    }

    public StateBuilder then(FsmAction... doActions)
    {
      _next.addActions(doActions);
      return _state._stateBuilder;
    }

    public OnBuilder on(String... eventList)
    {
      return _state._stateBuilder.on(eventList);
    }

    public StateBuilder state(String newState)
    {
      return _state._stateBuilder.state(newState);
    }

    public StateBuilder state(String name,FsmAction ...onStateActions)
    {
      return _state._stateBuilder.state(name, onStateActions);
    }

    public StateBuilder builder()
    {
      return _state._stateBuilder.builder();
    }
  }


}
