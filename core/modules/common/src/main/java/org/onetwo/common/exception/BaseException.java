package org.onetwo.common.exception;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.onetwo.common.reflect.ReflectUtils;

import com.google.common.collect.Maps;


@SuppressWarnings("serial")
public class BaseException extends RuntimeException implements SystemErrorCode, Serializable{
	
	public static BaseException formatMessage(String msg, Object...args){
		String formatMsg = String.format(msg, args);
		return new BaseException(formatMsg);
	}
	public static BaseException formatMessage(Throwable cause, String msg, Object...args){
		String formatMsg = String.format(msg, args);
		return new BaseException(formatMsg, cause);
	}

	private static final String SEP_LIE = "--------------------";
	
	protected static final String DefaultMsg = "[ERROR]";
//	public static final String Prefix = "[ERROR]";

	protected String code = getClass().getSimpleName();

	private Map<String, Object> errorContext;

//	protected List<Throwable> list = null;

	public BaseException() {
		super(DefaultMsg);
	}

	public BaseException(String msg) {
		super(msg);
	}

	public BaseException(Throwable cause) {
		super(DefaultMsg, cause);
		fillCauseErrorContext(cause);
	}

	public BaseException(ErrorType exceptionType) {
		super(exceptionType.getErrorMessage());
		this.code = exceptionType.getErrorCode();
	}

	public BaseException(ErrorType exceptionType, Throwable cause) {
		this(exceptionType.getErrorMessage(), cause, exceptionType.getErrorCode());
	}

	public BaseException(String msg, Throwable cause) {
		super(msg, cause);
		fillCauseErrorContext(cause);
//		super("[" + msg + "] : " + (cause==null?"":cause.getMessage()), cause);
	}

	public BaseException(String msg, Throwable cause, String code) {
		this(msg, cause);
		this.code = code;
	}
	
	@SuppressWarnings("unchecked")
	final public <T extends BaseException> T put(String key, Object value){
		errorContext().put(key, value);
		return (T)this;
	}
	
	@SuppressWarnings("unchecked")
	final public <T extends BaseException> T put(Object context){
		this.errorContext().put(context.getClass().getSimpleName(), context);
		return (T)this;
	}
	
	@SuppressWarnings("unchecked")
	final public <T extends BaseException> T putAsMap(Object context){
		try {
			Map<String, Object> map = ReflectUtils.toMap(context);
			this.errorContext().putAll(map);
		} catch (Exception e) {
			put(context);
		}
		return (T)this;
	}
	
	protected final void fillCauseErrorContext(Throwable cause){
		if(cause instanceof SystemErrorCode){
			SystemErrorCode se = (SystemErrorCode) cause;
			putAsMap(se.getErrorContext());
		}
	}
	

	private Map<String, Object> errorContext() {
		Map<String, Object> errorContext = this.errorContext;
		if(errorContext==null){
			errorContext = Maps.newHashMap();
			this.errorContext = errorContext;
		}
		return errorContext;
	}
	

    public void printStackTrace(PrintStream s) {
    	if(this.errorContext!=null){
    		StringBuilder contextMsg = new StringBuilder(300);
    		contextMsg.append(SEP_LIE).append("\n");
    		this.errorContext.forEach((key, value)->{
    			contextMsg.append(key).append("\t:\t").append(value);
    		});
    		contextMsg.append("\n").append(SEP_LIE);
    		s.println(contextMsg);
    	}
    	super.printStackTrace(s);
    }
    
	public Map<String, Object> getErrorContext() {
		return errorContext==null?Collections.emptyMap():errorContext;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [code=" + code + ", message=" + getMessage() + ", errorContext=" + errorContext + "]";
	}

	/****
	 * http://www.infoq.com/cn/articles/things-of-java-log-performance
	 */
    /*public Throwable fillInStackTrace() {
    	return this;
    }*/

}
