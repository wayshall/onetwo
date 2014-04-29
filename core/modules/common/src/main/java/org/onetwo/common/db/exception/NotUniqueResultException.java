package org.onetwo.common.db.exception;

import org.onetwo.common.exception.BaseException;

public class NotUniqueResultException extends BaseException {

	public NotUniqueResultException(int resultCount) {
		super("query did not return a unique result: " + resultCount );
	}

	public NotUniqueResultException(String msg, Exception e) {
		super(msg, e);
	}
}
