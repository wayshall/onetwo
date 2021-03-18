package org.onetwo.common.exception;
/**
 * @author wayshall
 * <br/>
 */
public class ErrorTypeImpl implements ErrorType {
	private final String errorCode;
	private final String errorMessage;
	private final Integer statusCode;
	public ErrorTypeImpl(String errorCode, String errorMessage,
			Integer statusCode) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public Integer getStatusCode() {
		return statusCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ErrorTypeImpl other = (ErrorTypeImpl) obj;
		if (errorCode == null) {
			if (other.errorCode != null)
				return false;
		} else if (!errorCode.equals(other.errorCode))
			return false;
		return true;
	}

}
