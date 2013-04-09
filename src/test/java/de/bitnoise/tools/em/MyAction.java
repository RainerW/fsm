package de.bitnoise.tools.em;

class MyAction implements EMAction {
	int _invoked;

	@Override
	public void doAction(EMState currentState, EMEvent eventObj) {
		_invoked++;
	}

	public int wasInvoked() {
		return _invoked;
	}
}