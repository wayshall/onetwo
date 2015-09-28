package org.onetwo.common.exception;


/***********
 * 
 * 发送短信错误
 * 异常代码参考{@link SystemErrorCode}定义
 * 
 * @author wayshall
 *
 */
public class SendSmsException extends BusinessException implements SystemErrorCode {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5608395998334544038L;

	public static final String DEFAULT_MESSAGE = "[send sms error 发送短信错误] ";


	public SendSmsException(String msg, String code) {
		super(DEFAULT_MESSAGE+msg);
		this.initErrorCode(code);
	}


	public SendSmsException(String msg, Throwable cause, String code) {
		super(msg, cause);
		this.initErrorCode(code);
	}


	public SendSmsException(String msg, Throwable cause) {
		super(msg, cause);
		this.initErrorCode(null);
	}


	public SendSmsException(String msg) {
		super(msg);
		this.initErrorCode(null);
	}


	public SendSmsException(Throwable cause, String code) {
		super(DEFAULT_MESSAGE, cause);
		this.initErrorCode(code);
	}


	public SendSmsException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
		this.initErrorCode(null);
	}
	
	protected String getBaseCode(){
		return ServiceErrorCode.SEND_SMS_ERROR;
	}


}
