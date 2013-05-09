package org.onetwo.common.spring.web.mvc;

import java.io.Serializable;

public class SingleReturnWrapper implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2967131553282203368L;
	
	public static SingleReturnWrapper wrap(Object value){
		return new SingleReturnWrapper(value);
	}
	
	private Object value;

	private SingleReturnWrapper(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}
	
}
