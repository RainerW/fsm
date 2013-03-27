package de.bitnoise.fsm.complex;

import java.util.List;

import de.bitnoise.fsm.simple.FsmAction;

public interface State
{
  String getSymbolicStateId();

  List<FsmAction> getOnStateActions();

  void setOnStateActions(FsmAction[] onStateActions);
}
