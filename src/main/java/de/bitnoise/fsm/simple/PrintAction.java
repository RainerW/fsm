package de.bitnoise.fsm.simple;

public class PrintAction implements FsmAction
{
  String _msg;
  public PrintAction(String msg) 
  {
    _msg=msg;
  }
  public void doAction(FsmEvent eventObj)
  {
    System.out.println(_msg);
  }
}
