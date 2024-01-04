package org.onetwo.ext.security.exception;

import jakarta.servlet.http.HttpServletResponse;

import org.onetwo.common.data.Result;

public interface ErrorMessageExtractor {

	void handleErrorResponse(HttpServletResponse response, Result result);
	
	SecurityErrorResult getErrorMessage(Exception throwable);
	
}
