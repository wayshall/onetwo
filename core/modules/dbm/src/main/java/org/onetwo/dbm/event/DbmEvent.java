package org.onetwo.dbm.event;

public interface DbmEvent<S> {

	DbmEventAction getAction();

	S getEventSource();

}