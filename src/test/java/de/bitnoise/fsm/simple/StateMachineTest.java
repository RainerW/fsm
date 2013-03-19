package de.bitnoise.fsm.simple;

import org.junit.Test;

import static org.fest.assertions.Assertions.*;

public class StateMachineTest
{
  protected FsmEvent a1_invoked;

  protected FsmEvent a2_invoked;

  protected FsmEvent a3_invoked;

  protected FsmEvent a4_invoked;

  FsmAction a1 = new FsmAction()
  {
    public void doAction(FsmEvent eventObj)
    {
      a1_invoked = eventObj;
    }
  };

  FsmAction a2 = new FsmAction()
  {
    public void doAction(FsmEvent eventObj)
    {
      a2_invoked = eventObj;
    }
  };

  FsmAction a3 = new FsmAction()
  {
    public void doAction(FsmEvent eventObj)
    {
      a3_invoked = eventObj;
    }
  };

  FsmAction a4 = new FsmAction()
  {
    public void doAction(FsmEvent eventObj)
    {
      a4_invoked = eventObj;
    }
  };

  StateMachine fsm = new StateMachine(new Object[][] {// @formatter:off
      // from       event      to         action 
      {"STARTED" , "SUCCESS" , "R1"    , a1},
      {"STARTED" , "FAIL"    , "R2"    , a2},
      {"DONE"    , "SUCCESS" , "R3"    , a3},
      {"DONE"    , "FAIL"    , "R4"    , a4},
  }); // @formatter:on

  @Test
  public void testTransitions()
  {
    assertThat(a1_invoked).isNull();
    assertThat(a2_invoked).isNull();
    assertThat(a3_invoked).isNull();
    assertThat(a4_invoked).isNull();

    String state = "STARTED";
    assertThat(fsm.event(state, "SUCCESS")).isEqualTo("R1");
    assertThat(fsm.event(state, "FAIL")).isEqualTo("R2");

    assertThat(a1_invoked).isNotNull();
    assertThat(a2_invoked).isNotNull();
    assertThat(a3_invoked).isNull();
    assertThat(a4_invoked).isNull();

    state = "DONE";
    assertThat(fsm.event(state, "SUCCESS")).isEqualTo("R3");
    assertThat(fsm.event(state, "FAIL")).isEqualTo("R4");

    assertThat(a1_invoked).isNotNull();
    assertThat(a2_invoked).isNotNull();
    assertThat(a3_invoked).isNotNull();
    assertThat(a4_invoked).isNotNull();
  }

  @Test
  public void testEvent()
  {
    assertThat(a1_invoked).isNull();
    assertThat(a2_invoked).isNull();
    assertThat(a3_invoked).isNull();
    assertThat(a4_invoked).isNull();
    FsmEvent SUCCESS = new Event<String>("SUCCESS");

    String state = "STARTED";
    assertThat(fsm.event(state, SUCCESS)).isEqualTo("R1");
    assertThat(fsm.event(state, "FAIL")).isEqualTo("R2");

    assertThat(a1_invoked).isNotNull();
    assertThat(a2_invoked).isNotNull();
    assertThat(a3_invoked).isNull();
    assertThat(a4_invoked).isNull();

    state = "DONE";
    assertThat(fsm.event(state, SUCCESS)).isEqualTo("R3");
    assertThat(fsm.event(state, "FAIL")).isEqualTo("R4");

    assertThat(a1_invoked).isNotNull();
    assertThat(a2_invoked).isNotNull();
    assertThat(a3_invoked).isNotNull();
    assertThat(a4_invoked).isNotNull();
  }

}
