package de.bitnoise.tools.em;

import java.util.List;

import de.bitnoise.tools.em.impl.EMEventImpl;
import de.bitnoise.tools.em.impl.EMStateImpl;
import de.bitnoise.tools.em.model.FsmModel;
import de.bitnoise.tools.em.model.FsmState;
import de.bitnoise.tools.em.model.FsmTransition;

public class EventMachine
{
  FsmModel _model;

  public EventMachine(FsmModel model)
  {
    _model = model;
  }

  public String getStartStateName()
  {
    return _model.getStartStateName();
  }

  public String start(EMState initialState)
  {
    String newState = getStartStateName();
    FsmState state = _model.getState(newState);
    if (state == null)
    {
      throwNewIllegalStateException(initialState, null, "Initial State is unknown, " + newState);
    }
    EMEventImpl event = new EMEventImpl("-generic-initial-start-event-");
    executActions(initialState, _model.getOnStateListeners(), event);
    executActions(initialState, state.getOnStateActions(), event);
    return newState;
  }

  public String start()
  {
    return start(null);
  }

  public String event(String stateName, String eventName)
  {
    return event(new EMStateImpl(stateName), new EMEventImpl(eventName));
  }

  public String event(EMState currentState, EMEvent<?> event)
  {
    String eventName = event.getEventName();
    String currentStateName = currentState.getStateName();
    FsmState state = _model.getState(currentStateName);
    if (state == null)
    {
      throwNewIllegalStateException(currentState, event,
          "Current State is unknown. " + currentStateName);
    }

    FsmTransition transtion = state.getTransitionFor(eventName);
    if (transtion == null)
    {
      throwNewIllegalStateException(currentState, event,
          "No Transition for Event '" + eventName + "' in state '"
              + currentStateName + "' known.");
      return "-illegal-state-reached-";
    } 
    else 
    {
      executActions(currentState, transtion.getActions(), event);
  
      String nextStateName = transtion.getNextState();
      FsmState nextState = _model.getState(nextStateName);
  
      if (!currentStateName.equals(nextStateName))
      {
        executActions(currentState, _model.getOnStateListeners(), event);
        if (nextState != null)
        {
          executActions(currentState, nextState.getOnStateActions(), event);
        }
      }
      return nextStateName;
    }
  }

  protected void executActions(EMState currentState, List<EMAction> actions, EMEvent eventObj)
  {
    for (EMAction action : actions)
    {
      action.doAction(currentState, eventObj);
    }
  }

  protected void throwNewIllegalStateException(EMState state, EMEvent event, String msg)
  {
    EMErrorHandler handler = _model.getErrorHandler();
    if (handler == null)
    {
      throw new IllegalStateException(msg);
    }
    handler.handleTransitionError(state, event, msg);
  }

}
