package org.onetwo.common.apiclient.utils;

import org.onetwo.common.exception.ErrorType;

/**
 * @author wayshall
 * <br/>
 */
public abstract class ApiClientConstants {
	
	public static enum ApiClientErrors implements ErrorType {
		REQUEST_MAPPING_NOT_PRESENT("@RequestMapping not present"),
		REQUEST_MAPPING_NOT_FOUND("@RequestMapping not found on method: %s"),
		REQUEST_BODY_ONLY_ONCE("There can be only one @RequstBody on method: %s"),
		HTTP_CLIENT_ERROR("http client error: %s"),
		EXECUTE_REST_ERROR("execute rest error: %s, method: %s"),
		CREATE_REST_ERROR("execute RestExecutor error"),
		CREATE_CLIENT_INST_ERROR("execute api client instance error");
		
		private String errorMessage;

		private ApiClientErrors(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		@Override
		public String getErrorCode() {
			return name();
		}

		@Override
		public String getErrorMessage() {
			return errorMessage;
		}
		
	}

}
