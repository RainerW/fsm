This is an experimental project to implement an state machine with Java.

[![Build Status](https://buildhive.cloudbees.com/job/RainerW/job/fsm/badge/icon)](https://buildhive.cloudbees.com/job/RainerW/job/fsm/)

# Getting started #

Complete sample code can be found in : [src/main/java/de/bitnoise/fsm/sample/EventMachineSample.java](src/main/java/de/bitnoise/fsm/sample/EventMachineSample.java)

## First, create a model of your State Machine ##

``` java
FsmModel fsm = new FsmModel();
fsm.addStartState("START")
    .addTransition("STARTED", "SEND-MAIL-SUCCESS");
fsm.addState("STARTED")
    .addTransition("LINKOPEN", "CONFIG-LINK-OPENED");
fsm.addState("LINKOPEN")
    .addTransition("SAVING", "SAVED");
fsm.addState("SAVING");
```

## Second, run the State Machine

``` java
// create state machine for the given model ( You can do this only once, because the EventMachine does not keep any state )
EventMachine em = new EventMachine(fsm); 

// To keep the state you have to provide an EMState implementation:
EMState myState = new EMStateImpl();

em.start(myState);
em.event(myState, "SEND-MAIL-SUCCESS");
em.event(myState, "CONFIG-LINK-OPENED");
em.event(myState, "SAVED");

// in case there is no known transition, a TransitionError is thrown:
// em.event(myState, "FAIL"); 
```

Above code should run without errors, but you will not see anything.

## Third, do some logging

``` java
// Add some methods 2 your class
void onEnterState(EMState state, EMEvent event)
{
  System.out.println("entering State  : " + state.getStateMemento());
}

void onTransition(EMState state, EMEvent event)
{
  System.out.println("transition from : " + state.getStateMemento() + " on " + event.getEventName());
}

// Helper to wrap class-methods into actions.
MethodsAsActions call = new MethodsAsActions(this);

// add some global listeners
fsm.setOnStateListeners(call.action("onEnterState"));
fsm.setOnTransitionListeners(call.action("onTransition"));
```

now the output should be something like:
``` java
transition from : null on -generic-initial-start-event-
entering State  : START
transition from : START on SEND-MAIL-SUCCESS
entering State  : STARTED
transition from : STARTED on CONFIG-LINK-OPENED
entering State  : LINKOPEN
transition from : LINKOPEN on SAVED
entering State  : SAVING
```