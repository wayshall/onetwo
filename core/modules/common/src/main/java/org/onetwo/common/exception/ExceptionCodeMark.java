package org.onetwo.common.exception;

public interface ExceptionCodeMark extends ExceptionMessageArgs {

	String getCode();
	Integer getStatusCode();
//	boolean isDefaultErrorCode();
}
