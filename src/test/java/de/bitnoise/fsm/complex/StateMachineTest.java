package de.bitnoise.fsm.complex;

import org.junit.Test;

import de.bitnoise.fsm.complex.StateMachineBuilder.StateBuilder;
import de.bitnoise.fsm.simple.FsmAction;
import de.bitnoise.fsm.simple.FsmEvent;

import static org.fest.assertions.Assertions.*;

public class StateMachineTest
{
  protected int l0_invoked;
  
  protected int r1_invoked;
  
  protected int r0_invoked;

  protected int r2_invoked;
  
  protected int r3_invoked;

  protected FsmEvent a1_invoked;

  protected FsmEvent a2_invoked;

  protected FsmEvent a3_invoked;

  protected FsmEvent a4_invoked;

  protected FsmEvent a5_invoked;

  FsmAction l0 = new FsmAction()
  {
    public void doAction(FsmEvent eventObj)
    {
      l0_invoked++;
    }
  };
  
  FsmAction r0 = new FsmAction()
  {
    public void doAction(FsmEvent eventObj)
    {
      r0_invoked++;
    }
  };
  
  FsmAction r1 = new FsmAction()
  {
    public void doAction(FsmEvent eventObj)
    {
      r1_invoked++;
    }
  };

  FsmAction r2 = new FsmAction()
  {
    public void doAction(FsmEvent eventObj)
    {
      r2_invoked++;
    }
  };
  
  FsmAction r3 = new FsmAction()
  {
    public void doAction(FsmEvent eventObj)
    {
      r3_invoked++;
    }
  };

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
      .startState("STARTED",r0)
         .on("SUCCESS")   .goTo("R1").then(a1)
         .on("FAIL")      .goTo("R2").then(a2)
      .state("DONE") 
         .on("SUCCESS")   .goTo("R3").then(a3)
         .on("FAIL")      .goTo("R4").then(a4)
      .state("R1",r1) 
         .on("SUCCESS")   .goTo("R2")
         .on("FAIL")      .goTo("R1")
         .on("TO3")       .goTo("R3")
      .state("R2",r2) 
         .on("SUCCESS")   .goTo("R1")
      .state("R3",r3)
      .setOnStateListeners(l0)
      .builder();
  // @formatter:on
  StateMachine fsm = x.createStateMachine();

  @Test
  public void testStarting()
  {
    assertThat(l0_invoked).isEqualTo(0);
    assertThat(r0_invoked).isEqualTo(0);
    assertThat(r1_invoked).isEqualTo(0);
    assertThat(r2_invoked).isEqualTo(0);
    assertThat(r3_invoked).isEqualTo(0);
    
    assertThat(fsm.start()).isEqualTo("STARTED");
    
    assertThat(l0_invoked).isEqualTo(1);
    assertThat(r0_invoked).isEqualTo(1);
    assertThat(r1_invoked).isEqualTo(0);
    assertThat(r2_invoked).isEqualTo(0);
    assertThat(r3_invoked).isEqualTo(0);
  }
  
  @Test
  public void testTransitions()
  {
    assertThat(a1_invoked).isNull();
    assertThat(a2_invoked).isNull();
    assertThat(a3_invoked).isNull();
    assertThat(a4_invoked).isNull();
    assertThat(l0_invoked).isEqualTo(0);
    assertThat(r1_invoked).isEqualTo(0);
    assertThat(r2_invoked).isEqualTo(0);
    assertThat(r3_invoked).isEqualTo(0);

    String state = "STARTED";
    assertThat(fsm.event(state, "SUCCESS").toString()).isEqualTo("R1");
    assertThat(fsm.event(state, "FAIL").toString()).isEqualTo("R2");

    assertThat(a1_invoked).isNotNull();
    assertThat(a2_invoked).isNotNull();
    assertThat(a3_invoked).isNull();
    assertThat(a4_invoked).isNull();
    assertThat(l0_invoked).isEqualTo(2);
    assertThat(r1_invoked).isEqualTo(1);
    assertThat(r2_invoked).isEqualTo(1);
    assertThat(r3_invoked).isEqualTo(0);

    state = "DONE";
    assertThat(fsm.event(state, "SUCCESS").toString()).isEqualTo("R3");
    assertThat(fsm.event(state, "FAIL").toString()).isEqualTo("R4");

    assertThat(a1_invoked).isNotNull();
    assertThat(a2_invoked).isNotNull();
    assertThat(a3_invoked).isNotNull();
    assertThat(a4_invoked).isNotNull();
    assertThat(l0_invoked).isEqualTo(4);
    assertThat(r1_invoked).isEqualTo(1);
    assertThat(r2_invoked).isEqualTo(1);
    assertThat(r3_invoked).isEqualTo(1);
  }
  
  @Test
  public void testStateEvents() 
  {
    assertThat(l0_invoked).isEqualTo(0);
    assertThat(r1_invoked).isEqualTo(0);
    assertThat(r2_invoked).isEqualTo(0);
    assertThat(r2_invoked).isEqualTo(0);
    
    assertThat(fsm.event("R1", "FAIL").toString()).isEqualTo("R1");
    
    assertThat(l0_invoked).isEqualTo(0);
    assertThat(r1_invoked).isEqualTo(0);
    assertThat(r2_invoked).isEqualTo(0);
    assertThat(r2_invoked).isEqualTo(0);

    assertThat(fsm.event("R1", "SUCCESS").toString()).isEqualTo("R2");
    
    assertThat(l0_invoked).isEqualTo(1);
    assertThat(r1_invoked).isEqualTo(0);
    assertThat(r2_invoked).isEqualTo(1);
    assertThat(r3_invoked).isEqualTo(0);
    
    assertThat(fsm.event("R2", "SUCCESS").toString()).isEqualTo("R1");
    
    assertThat(l0_invoked).isEqualTo(2);
    assertThat(r1_invoked).isEqualTo(1);
    assertThat(r2_invoked).isEqualTo(1);
    assertThat(r3_invoked).isEqualTo(0);
    
    assertThat(fsm.event("R1", "SUCCESS").toString()).isEqualTo("R2");
    
    assertThat(l0_invoked).isEqualTo(3);
    assertThat(r1_invoked).isEqualTo(1);
    assertThat(r2_invoked).isEqualTo(2);
    assertThat(r3_invoked).isEqualTo(0);
    
    assertThat(fsm.event("R1", "FAIL").toString()).isEqualTo("R1");
    assertThat(fsm.event("R1", "TO3").toString()).isEqualTo("R3");
    
    assertThat(l0_invoked).isEqualTo(4);
    assertThat(r1_invoked).isEqualTo(1);
    assertThat(r2_invoked).isEqualTo(2);
    assertThat(r3_invoked).isEqualTo(1);
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
