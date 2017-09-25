package org.onetwo.common.utils;
/**
 * @author wayshall
 * <br/>
 */
public class ValueHolder<T> {
	
	private T value;
	
	public ValueHolder(T value) {
		super();
		this.value = value;
	}
	
	public boolean hasValue(){
		return this.value!=null;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
}
