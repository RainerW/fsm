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
      throwNewIllegalStateException("Current State is unknown. " + currentStateName);
    }

    FsmTransition transtion = state.getTransitionFor(eventName);
    if (transtion == null)
    {
      throwNewIllegalStateException("No Transition for Event '" + eventName + "' in state '" + currentStateName + "' known.");
    }

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

  protected void executActions(EMState currentState, List<EMAction> actions, EMEvent eventObj)
  {
    for (EMAction action : actions)
    {
      action.doAction(currentState, eventObj);
    }
  }

  protected void throwNewIllegalStateException(String msg)
  {
    throw new IllegalStateException(msg);
  }

}
