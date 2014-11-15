package org.onetwo.common.utils;

public class JResult<T> {
	
	public static <E> JResult<E> error(String message){
		return new JResult<E>(null, message, true);
	}
	
	public static <E> JResult<E> wrap(E value){
		return new JResult<E>(value, null, false);
	}
	
	private final T value;
	private final String message;
	private final boolean error;
	
	private JResult(T value, String message, boolean error) {
		super();
		this.value = value;
		this.message = message;
		this.error = error;
	}

	public T getValue() {
		return value;
	}

	public String getMessage() {
		return message;
	}

	public boolean isError() {
		return error;
	}
	

}
