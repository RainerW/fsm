package de.bitnoise.fsm.simple;

public interface FsmAction<E extends FsmEvent<?>>
{

  void doAction(E eventObj);

}
