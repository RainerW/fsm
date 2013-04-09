package de.bitnoise.fsm.sample;

import org.junit.Test;

import de.bitnoise.tools.em.EMEvent;
import de.bitnoise.tools.em.EMState;
import de.bitnoise.tools.em.EventMachine;
import de.bitnoise.tools.em.MethodsAsActions;
import de.bitnoise.tools.em.TransitionError;
import de.bitnoise.tools.em.impl.EMStateImpl;
import de.bitnoise.tools.em.model.FsmModel;

public class EventMachineSample
{
  @Test
  public void sample() throws TransitionError
  {
    // prepare
    MethodsAsActions call = new MethodsAsActions(this);
    FsmModel fsm = new FsmModel();
    fsm.addStartState("START")
        .addTransition("STARTED", "SEND-MAIL-SUCCESS");
    fsm.addState("STARTED")
        .addTransition("LINKOPEN", "CONFIG-LINK-OPENED");
    fsm.addState("LINKOPEN")
        .addTransition("SAVING", "SAVED");
    fsm.addState("SAVING");

    // print out onState/onTranstion messages
    fsm.setOnStateListeners(call.action("onEnterState"));
    fsm.setOnTransitionListeners(call.action("onTransition"));

    // execute
    EMState myState = new EMStateImpl();
    EventMachine em = new EventMachine(fsm);
    em.start(myState);
    em.event(myState, "SEND-MAIL-SUCCESS");
    em.event(myState, "CONFIG-LINK-OPENED");
    em.event(myState, "SAVED");
    // em.event(myState, "FAIL"); this would cause an TransitionError

    // verify
    /**
     * output should be :
     * 
     * <pre>
     * transition from : null on -generic-initial-start-event-
     * entering State  : START
     * transition from : START on SEND-MAIL-SUCCESS
     * entering State  : STARTED
     * transition from : STARTED on CONFIG-LINK-OPENED
     * entering State  : LINKOPEN
     * transition from : LINKOPEN on SAVED
     * entering State  : SAVING
     * </pre>
     */
  }

  void onEnterState(EMState state, EMEvent event)
  {
    System.out.println("entering State  : " + state.getStateMemento());
  }

  void onTransition(EMState state, EMEvent event)
  {
    System.out.println("transition from : " + state.getStateMemento() + " on " + event.getEventName());
  }

}
