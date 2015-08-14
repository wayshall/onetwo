package org.onetwo.common.db.filequery;

import org.onetwo.common.exception.BaseException;

@SuppressWarnings("serial")
public class FileNamedQueryException extends BaseException {

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
