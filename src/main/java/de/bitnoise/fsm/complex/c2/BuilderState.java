package de.bitnoise.fsm.complex.c2;

import java.util.List;

import de.bitnoise.fsm.simple.FsmAction;

public interface BuilderState
{
  String getStateName();

  List<FsmAction> getOnStateActions();

  void setOnStateActions(FsmAction[] onStateActions);
}
