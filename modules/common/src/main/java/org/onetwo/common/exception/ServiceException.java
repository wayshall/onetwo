package org.onetwo.common.exception;

/*********
 * 
 * @author wayshall
 *
 */
//@Deprecated
public class ServiceException extends BaseException{

	/**
	 *  
	 */
	private static final long serialVersionUID = 7280411050853219784L;

	public ServiceException() {
		super();
	} 


	public ServiceException(String msg, String code) {
		super(msg, code);
	}

	public ServiceException(String msg, Throwable cause, String code) {
		super(msg, cause, code);
	}


	public ServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}


	public ServiceException(String msg) {
		super(msg);
	}


	public ServiceException(Throwable cause, String code) {
		super(cause, code);
	}
	
	protected String getBaseCode(){
		return BaseErrorCode.BASE_CODE;
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}
	
	protected void setErrorCode(){
		this.code = BaseErrorCode.BASE_CODE;
	}

}
