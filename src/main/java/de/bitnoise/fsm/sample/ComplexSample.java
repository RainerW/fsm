package de.bitnoise.fsm.sample;

import de.bitnoise.fsm.complex.FsmStateImpl;
import de.bitnoise.fsm.complex.State;
import de.bitnoise.fsm.complex.StateMachine;
import de.bitnoise.fsm.complex.StateMachineBuilder;
import de.bitnoise.fsm.complex.StateMachineBuilder.StateBuilder;
import de.bitnoise.fsm.simple.PrintAction;

/**
 * 
 * Plant uml state diagram <code>
 * 
 * @startuml
 * 
 *           [*] -> START
 * 
 *           START -down-> STARTING: START STARTING -> ERROR: SEND-MAIL-ERROR
 *           STARTING -down-> STARTED : SEND-MAIL-SUCCESS STARTED -down->
 *           LINKOPEN : CONFIG-LINK-OPENED
 * 
 *           ERROR_SAVE -> LINKOPEN : CONFIG-LINK-OPENED LINKOPEN -> LINKOPEN :
 *           CONFIG-LINK-OPENED LINKOPEN -> LINKOPEN :CANCEL LINKOPEN -down->
 *           SAVING :SAVED SAVING -> ERROR_SAVE : SEND-MAIL-ERROR SAVING -down->
 *           SUCCESS :SEND-MAIL-SUCCESS SUCCESS-> [*] ERROR-> [*]
 * @enduml </code>
 * 
 * 
 */
public class ComplexSample
{
  static State start = new FsmStateImpl("start");
  static State starting = new FsmStateImpl("starting");
  static State started = new FsmStateImpl("started");
  static State errorOnSave = new FsmStateImpl("errorOnSave");
  static State linkOpened = new FsmStateImpl("linkOpened");
  static State saving = new FsmStateImpl("saving");
  static State success = new FsmStateImpl("success");
  static State error = new FsmStateImpl("error");
  
  static StateBuilder fsb = StateMachineBuilder
          .state(start)
             .on("START").goTo(starting)
          .state(starting)
             .on("SEND-MAIL-ERROR")   .goTo(error)       .then( new PrintAction("sending subscription mail") )           
             .on("SEND-MAIL-SUCCESS") .goTo(started)     .then( new PrintAction("sending subscription mail succeeded"))
          .state(started)
             .on("CONFIG-LINK-OPENED").goTo(linkOpened)  .then( new PrintAction("user opend link from mail") )   
          .state(errorOnSave)
             .on("CONFIG-LINK-OPENED").goTo(linkOpened)  .then( new PrintAction("user opend link from mail") )   
          .state(linkOpened)
             .on("CONFIG-LINK-OPENED","CANCEL").goTo(linkOpened)
             .on("SAVED")             .goTo(saving)      .then( new PrintAction("sending mail with new account") )
          .state(saving)
             .on("SEND-MAIL-ERROR")   .goTo(errorOnSave) .then( new PrintAction("Confirmation mail send with error") )
             .on("SEND-MAIL-SUCCESS") .goTo(success)     .then( new PrintAction("Confirmation mail send sucessfully") )
          .builder()
          ;
  
//  static StateMachine fsm = new StateMachine(new Object[][] {// @formatter:off
//      // from          event      to         action 
//      {start      , "START"                 , starting   , new PrintAction("sending subscription mail")},
//      {starting   , "SEND-MAIL-ERROR"       , error      , new PrintAction("sending subscription mail failed")},
//      {starting   , "SEND-MAIL-SUCCESS"     , started    , new PrintAction("sending subscription mail succeeded")},
//      {started    , "CONFIG-LINK-OPENED"    , linkOpened   , new PrintAction("user opend link from mail")},
//      {errorOnSave, "CONFIG-LINK-OPENED"    , linkOpened   , new PrintAction("user opend link from mail")},
//      {linkOpened , "CONFIG-LINK-OPENED"    , linkOpened   , new PrintAction("user opend link from mail")},
//      {linkOpened , "CANCEL"                , linkOpened   , new NullAction() }, // coz user just pressed cancel
//      {linkOpened , "SAVED"                 , saving     , new PrintAction("sending mail with new account")},
//      {saving     , "SEND-MAIL-ERROR"       , errorOnSave , new PrintAction("There was an Error when sending the mail")},
//      {saving     , "SEND-MAIL-SUCCESS"     , success    , new PrintAction("There was an Error when sending the mail")},
//  }); // @formatter:on

  public static void main(String[] args)
  {
    StateMachine fsm = fsb.createStateMachine(); 
    // user requests 2 subscribe
    // so we send an mail
    State state = fsm.event(start, "START");
    // the mailserver tell's us it was sucessfully sended
    state = fsm.event(state, "SEND-MAIL-SUCCESS");
    // the embedded link was opened
    state = fsm.event(state, "CONFIG-LINK-OPENED");
    // the form was canceled -> no output beacuse only state changed
    state = fsm.event(state, "CANCEL");
    // the embedded link was openend again
    state = fsm.event(state, "CONFIG-LINK-OPENED");
    // the form was saved -> we send a confirmation mail
    state = fsm.event(state, "SAVED");
    // the mailserver tell's us it was sucessfully sended
    state = fsm.event(state, "SEND-MAIL-SUCCESS");
  }
}
