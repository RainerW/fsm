package de.bitnoise.fsm.sample;

import de.bitnoise.fsm.simple.NullAction;
import de.bitnoise.fsm.simple.PrintAction;
import de.bitnoise.fsm.simple.StateMachine;

/**
 * 
 * Plant uml state diagram
   <code>
    @startuml
 
	[*] -> START      
	
	START      -down-> STARTING: START
	STARTING   -> ERROR: SEND-MAIL-ERROR
	STARTING   -down-> STARTED : SEND-MAIL-SUCCESS
	STARTED    -down-> LINKOPEN : CONFIG-LINK-OPENED
	
	ERROR_SAVE -> LINKOPEN : CONFIG-LINK-OPENED
	LINKOPEN   -> LINKOPEN : CONFIG-LINK-OPENED
	LINKOPEN   -> LINKOPEN   :CANCEL
	LINKOPEN   -down-> SAVING     :SAVED
	SAVING     -> ERROR_SAVE : SEND-MAIL-ERROR
	SAVING     -down-> SUCCESS    :SEND-MAIL-SUCCESS
	SUCCESS-> [*]
	ERROR-> [*]
	@enduml
	</code>

 *
 */
public class SimpleSample {
	static StateMachine fsm = new StateMachine(new Object[][] {// @formatter:off
		      // from       event      to         action 
			  {"START"      , "START"                 , "STARTING"   , new PrintAction("sending subscription mail")},
		      {"STARTING"   , "SEND-MAIL-ERROR"       , "ERROR"      , new PrintAction("sending subscription mail failed")},
		      {"STARTING"   , "SEND-MAIL-SUCCESS"     , "STARTED"    , new PrintAction("sending subscription mail succeeded")},
		      {"STARTED"    , "CONFIG-LINK-OPENED"    , "LINKOPEN"   , new PrintAction("user opend link from mail")},
		      {"ERROR-SAVE" , "CONFIG-LINK-OPENED"    , "LINKOPEN"   , new PrintAction("user opend link from mail")},
		      {"LINKOPEN"   , "CONFIG-LINK-OPENED"    , "LINKOPEN"   , new PrintAction("user opend link from mail")},
		      {"LINKOPEN"   , "CANCEL"                , "LINKOPEN"   , new NullAction() }, // coz user just pressed cancel
		      {"LINKOPEN"   , "SAVED"                 , "SAVING"     , new PrintAction("sending mail with new account")},
		      {"SAVING"     , "SEND-MAIL-ERROR"       , "ERROR-SAVE" , new PrintAction("There was an Error when sending the mail")},
		      {"SAVING"     , "SEND-MAIL-SUCCESS"     , "SUCCESS"    , new PrintAction("There was an Error when sending the mail")},
		  }); // @formatter:on

	public static void main(String[] args) {
		
		// user requests 2 subscribe
		// so we send an mail
		String state = fsm.event("START", "START");
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
