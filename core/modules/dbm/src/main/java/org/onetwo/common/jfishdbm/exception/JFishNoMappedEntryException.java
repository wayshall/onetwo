package org.onetwo.common.jfishdbm.exception;


@SuppressWarnings("serial")
public class JFishNoMappedEntryException extends JFishOrmException{

	public JFishNoMappedEntryException() {
		super("no mapped entry !");
	}

	public JFishNoMappedEntryException(String msg) {
		super("[no mapped entry] "+msg);
	}


}
