package org.onetwo.common.apiclient.utils;

import org.onetwo.common.exception.ErrorType;

/**
 * @author wayshall
 * <br/>
 */
public abstract class ApiClientConstants {
	
	public static enum ApiClientError implements ErrorType {
		REQUEST_MAPPING_NOT_PRESENT("@RequestMapping not present"),
		REQUEST_MAPPING_NOT_FOUND("@RequestMapping not found on method: %s"),
		EXECUTE_REST_ERROR("execute rest error for interface: %s");
		
		private String errorMessage;

		private ApiClientError(String errorMessage) {
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