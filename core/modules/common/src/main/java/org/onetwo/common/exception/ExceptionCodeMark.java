package org.onetwo.common.exception;

import java.util.Optional;

public interface ExceptionCodeMark extends ExceptionMessageArgs {

	String getCode();
	default Optional<Integer> getStatusCode() {
		return Optional.empty();
	}
//	boolean isDefaultErrorCode();
}
