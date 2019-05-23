package org.onetwo.common.data;

import java.io.Serializable;

/*****
 * @author wayshall
 * 
 */
@SuppressWarnings("serial")
public class ValueWrapper<T> implements Serializable{

	public static <E> ValueWrapper<E> wrap(E value){
		return new ValueWrapper<E>(value);
	}
	public static <E> ValueWrapper<E> create(){
		return new ValueWrapper<E>(null);
	}
	
	private T value;

	private ValueWrapper(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
}
