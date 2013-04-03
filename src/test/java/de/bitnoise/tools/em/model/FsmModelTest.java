package de.bitnoise.tools.em.model;

import static org.fest.assertions.Assertions.*;

import org.junit.Test;

import de.bitnoise.tools.em.EMEvent;
import de.bitnoise.tools.em.EMState;
import de.bitnoise.tools.em.EMAction;
import de.bitnoise.tools.em.model.FsmModel;
import de.bitnoise.tools.em.model.FsmState;
import de.bitnoise.tools.em.model.FsmTransition;

public class FsmModelTest
{
  EMAction aAction = new EMAction<EMState, EMEvent<?>>()
  {
    @Override
    public void doAction(EMState currentState, EMEvent<?> eventObj)
    {
    }
  };

  @Test
  public void build()
  {
    FsmModel sut = new FsmModel();
    sut.addState("START")
        .addTransition("WORKING", "SUCCESS")
        .addTransition("ERROR", "FAIL");
    sut.addState("START")
        .addTransition("WORKING", "SUCCESS", aAction)
        .addTransition("ERROR", "FAIL");
  }

  @Test
  public void addState()
  {
    FsmModel sut = new FsmModel();

    FsmState state1 = sut.addState("START");
    assertThat(state1.getName()).isEqualTo("START");

    FsmState state2 = sut.addState("sTART");
    assertThat(state2.getName()).isEqualTo("sTART");
    assertThat(state1).isNotSameAs(state2);

    FsmState state3 = sut.addState("START");
    assertThat(state3.getName()).isEqualTo("START");
    assertThat(state1).isSameAs(state3);
  }

  @Test
  public void addTransition()
  {
    FsmModel sut = new FsmModel();
    FsmState state = sut.addState("START");

    // execute
    state.addTransition("NEXT", new String[] {"Event1", "Event2"}, new EMAction[] {aAction});

    // verify
    FsmTransition transition1 = state.getTransitionFor("Event1");
    assertThat(transition1).isNotNull();
    assertThat(transition1.getNextState()).isEqualTo("NEXT");
    assertThat(transition1.getActions()).hasSize(1);

    // verify
    FsmTransition transition2 = state.getTransitionFor("Event2");
    assertThat(transition2).isNotNull();
    assertThat(transition2.getNextState()).isEqualTo("NEXT");
    assertThat(transition2.getActions()).hasSize(1);

    // verify non existing event
    FsmTransition noTransition = state.getTransitionFor("EvenT1");
    assertThat(noTransition).isNull();
  }
}
