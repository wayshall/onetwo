package org.onetwo.common.jfishdb.exception;


@SuppressWarnings("serial")
public class JFishNoMappedEntryException extends JFishOrmException{

	public JFishNoMappedEntryException() {
		super("no mapped entry !");
	}

	public JFishNoMappedEntryException(String msg) {
		super("[no mapped entry] "+msg);
	}


}
