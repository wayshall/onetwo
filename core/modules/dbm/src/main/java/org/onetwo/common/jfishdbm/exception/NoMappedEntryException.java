package org.onetwo.common.jfishdbm.exception;


@SuppressWarnings("serial")
public class NoMappedEntryException extends DbmException{

	public NoMappedEntryException() {
		super("no mapped entry !");
	}

	public NoMappedEntryException(String msg) {
		super("[no mapped entry] "+msg);
	}


}
