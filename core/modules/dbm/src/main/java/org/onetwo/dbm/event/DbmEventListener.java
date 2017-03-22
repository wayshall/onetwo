package org.onetwo.dbm.event;

public interface DbmEventListener<S, E extends DbmEvent<S>> {
	
	public void doEvent(E event);

}
