package de.bitnoise.tools.em;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

import de.bitnoise.tools.em.EventMachine;
import de.bitnoise.tools.em.model.FsmModel;

public class EventMachineTest
{
  class MyAction implements EMAction
  {
    int _invoked;

    @Override
    public void doAction(EMState currentState, EMEvent eventObj)
    {
      _invoked++;
    }

    public int wasInvoked()
    {
      return _invoked;
    }
  }

  MyAction a1 = new MyAction();
  MyAction a2 = new MyAction();
  MyAction a3 = new MyAction();
  MyAction a4 = new MyAction();
  MyAction a5 = new MyAction();
  MyAction a6 = new MyAction();
  
  MyAction r1 = new MyAction();
  MyAction r2 = new MyAction();
  MyAction r3 = new MyAction();
  
  MyAction l0 = new MyAction();

  EventMachine fsm;
  {
    FsmModel model = new FsmModel();
    model.addState("STARTED")
        .addTransition("R1", "SUCCESS", a1)
        .addTransition("R2", "FAIL", a2);
    model.addState("DONE")
        .addTransition("R3", "SUCCESS", a3)
        .addTransition("R4", "FAIL", a4);
    model.addState("R1",r1)
        .addTransition("R2", "SUCCESS")
        .addTransition("R1", "FAIL")
        .addTransition("R3", "TO3");
    model.addState("R2",r2)
        .addTransition("R1", "SUCCESS");
    model.addState("R3",r3);
    model.setOnStateListeners(l0);
    fsm = new EventMachine(model);
  }

  @Test
  public void testTransitions()
  {
    assertThat(a1.wasInvoked()).isEqualTo(0);
    assertThat(a2.wasInvoked()).isEqualTo(0);
    assertThat(a3.wasInvoked()).isEqualTo(0);
    assertThat(a4.wasInvoked()).isEqualTo(0);
    assertThat(l0.wasInvoked()).isEqualTo(0);
    assertThat(r1.wasInvoked()).isEqualTo(0);
    assertThat(r2.wasInvoked()).isEqualTo(0);
    assertThat(r3.wasInvoked()).isEqualTo(0);

    String state = "STARTED";
    assertThat(fsm.event(state, "SUCCESS").toString()).isEqualTo("R1");
    assertThat(fsm.event(state, "FAIL").toString()).isEqualTo("R2");

    assertThat(a1.wasInvoked()).isEqualTo(1);
    assertThat(a2.wasInvoked()).isEqualTo(1);
    assertThat(a3.wasInvoked()).isEqualTo(0);
    assertThat(a4.wasInvoked()).isEqualTo(0);
    assertThat(l0.wasInvoked()).isEqualTo(2);
    assertThat(r1.wasInvoked()).isEqualTo(1);
    assertThat(r2.wasInvoked()).isEqualTo(1);
    assertThat(r3.wasInvoked()).isEqualTo(0);

    state = "DONE";
    assertThat(fsm.event(state, "SUCCESS").toString()).isEqualTo("R3");
    assertThat(fsm.event(state, "FAIL").toString()).isEqualTo("R4");

    assertThat(a1.wasInvoked()).isEqualTo(1);
    assertThat(a2.wasInvoked()).isEqualTo(1);
    assertThat(a3.wasInvoked()).isEqualTo(1);
    assertThat(a4.wasInvoked()).isEqualTo(1);
    assertThat(l0.wasInvoked()).isEqualTo(4);
    assertThat(r1.wasInvoked()).isEqualTo(1);
    assertThat(r2.wasInvoked()).isEqualTo(1);
    assertThat(r3.wasInvoked()).isEqualTo(1);
  }
}
