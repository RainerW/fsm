package de.bitnoise.fsm.complex;

import org.junit.Test;

import de.bitnoise.fsm.complex.StateMachineBuilder.StateBuilder;
import de.bitnoise.fsm.simple.FsmAction;
import de.bitnoise.fsm.simple.FsmEvent;

import static org.fest.assertions.Assertions.*;

public class StateMachineTest
{
  protected FsmEvent a1_invoked;

  protected FsmEvent a2_invoked;

  protected FsmEvent a3_invoked;

  protected FsmEvent a4_invoked;

  protected FsmEvent a5_invoked;

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
  FsmAction a5 = new FsmAction()
  {
    public void doAction(FsmEvent eventObj)
    {
      a5_invoked = eventObj;
    }
  };

  StateBuilder x = StateMachineBuilder// @formatter:off
      .state("STARTED")
         .on("SUCCESS")   .goTo("R1").then(a1)
         .on("FAIL")      .goTo("R2").then(a2)
      .state("DONE") 
         .on("SUCCESS")   .goTo("R3").then(a3)
         .on("FAIL")      .goTo("R4").then(a4)
      .builder();
  // @formatter:on
  StateMachine fsm = x.createStateMachine();

  @Test
  public void testTransitions()
  {
    assertThat(a1_invoked).isNull();
    assertThat(a2_invoked).isNull();
    assertThat(a3_invoked).isNull();
    assertThat(a4_invoked).isNull();

    String state = "STARTED";
    assertThat(fsm.event(state, "SUCCESS").toString()).isEqualTo("R1");
    assertThat(fsm.event(state, "FAIL").toString()).isEqualTo("R2");

    assertThat(a1_invoked).isNotNull();
    assertThat(a2_invoked).isNotNull();
    assertThat(a3_invoked).isNull();
    assertThat(a4_invoked).isNull();

    state = "DONE";
    assertThat(fsm.event(state, "SUCCESS").toString()).isEqualTo("R3");
    assertThat(fsm.event(state, "FAIL").toString()).isEqualTo("R4");

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
    assertThat(fsm.event(state, SUCCESS).toString()).isEqualTo("R1");
    assertThat(fsm.event(state, "FAIL").toString()).isEqualTo("R2");

    assertThat(a1_invoked).isNotNull();
    assertThat(a2_invoked).isNotNull();
    assertThat(a3_invoked).isNull();
    assertThat(a4_invoked).isNull();

    state = "DONE";
    assertThat(fsm.event(state, SUCCESS).toString()).isEqualTo("R3");
    assertThat(fsm.event(state, "FAIL").toString()).isEqualTo("R4");

    assertThat(a1_invoked).isNotNull();
    assertThat(a2_invoked).isNotNull();
    assertThat(a3_invoked).isNotNull();
    assertThat(a4_invoked).isNotNull();
  }

}
