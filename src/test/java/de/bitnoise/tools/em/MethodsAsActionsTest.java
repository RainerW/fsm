package de.bitnoise.tools.em;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.bitnoise.tools.em.impl.EMStateImpl;
import de.bitnoise.tools.em.model.FsmModel;

public class MethodsAsActionsTest
{

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

  protected void a1(EMState state, EMEvent evt)
  {
    a1.doAction(state, evt);
  }

  protected void a2(EMState state, EMEvent evt)
  {
    a2.doAction(state, evt);
  }

  protected void a3(EMState state, EMEvent evt)
  {
    a3.doAction(state, evt);
  }

  protected void a4(EMState state, EMEvent evt)
  {
    a4.doAction(state, evt);
  }

  protected void a5(EMState state, EMEvent evt)
  {
    a5.doAction(state, evt);
  }

  protected void a6(EMState state, EMEvent evt)
  {
    a6.doAction(state, evt);
  }

  protected void r1(EMState state, EMEvent evt)
  {
    r1.doAction(state, evt);
  }

  protected void r2(EMState state, EMEvent evt)
  {
    r2.doAction(state, evt);
  }

  protected void r3(EMState state, EMEvent evt)
  {
    r3.doAction(state, evt);
  }

  protected void l0(EMState state, EMEvent evt)
  {
    l0.doAction(state, evt);
  }

  EventMachine fsm;

  @Before
  public void initFsm()
  {
    MethodsAsActions call = new MethodsAsActions(this);
    FsmModel model = new FsmModel();
    model.addState("STARTED")
        .addTransition("R1", "SUCCESS", call.action("a1"))
        .addTransition("R2", "FAIL", call.action("a2"));
    model.addState("DONE")
        .addTransition("R3", "SUCCESS", call.action("a3"))
        .addTransition("R4", "FAIL", call.action("a4"));
    model.addState("R1", call.action("r1"))
        .addTransition("R2", "SUCCESS")
        .addTransition("R1", "FAIL")
        .addTransition("R3", "TO3");
    model.addState("R2", call.action("r2"))
        .addTransition("R1", "SUCCESS");
    model.addState("R3", call.action("r3"));
    model.setOnStateListeners(call.action("l0"));
    fsm = new EventMachine(model);
  }

  @Test
  public void testTransitions() throws TransitionError
  {
    assertThat(a1.wasInvoked()).isEqualTo(0);
    assertThat(a2.wasInvoked()).isEqualTo(0);
    assertThat(a3.wasInvoked()).isEqualTo(0);
    assertThat(a4.wasInvoked()).isEqualTo(0);
    assertThat(l0.wasInvoked()).isEqualTo(0);
    assertThat(r1.wasInvoked()).isEqualTo(0);
    assertThat(r2.wasInvoked()).isEqualTo(0);
    assertThat(r3.wasInvoked()).isEqualTo(0);

    EMState state = new EMStateImpl();
    state.setStateMemento("STARTED");
    assertThat(fsm.event(state, "SUCCESS").toString()).isEqualTo("R1");
    state.setStateMemento("STARTED");
    assertThat(fsm.event(state, "FAIL").toString()).isEqualTo("R2");

    assertThat(a1.wasInvoked()).isEqualTo(1);
    assertThat(a2.wasInvoked()).isEqualTo(1);
    assertThat(a3.wasInvoked()).isEqualTo(0);
    assertThat(a4.wasInvoked()).isEqualTo(0);
    assertThat(l0.wasInvoked()).isEqualTo(2);
    assertThat(r1.wasInvoked()).isEqualTo(1);
    assertThat(r2.wasInvoked()).isEqualTo(1);
    assertThat(r3.wasInvoked()).isEqualTo(0);

    state.setStateMemento("DONE");
    assertThat(fsm.event(state, "SUCCESS").toString()).isEqualTo("R3");
    state.setStateMemento("DONE");
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
