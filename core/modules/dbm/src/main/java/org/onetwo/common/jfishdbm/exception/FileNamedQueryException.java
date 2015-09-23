package org.onetwo.common.jfishdbm.exception;


@SuppressWarnings("serial")
public class FileNamedQueryException extends QueryException {

	public FileNamedQueryException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public FileNamedQueryException(String msg) {
		super(msg);
	}

	public FileNamedQueryException(Throwable cause) {
		super(cause);
	}

}
