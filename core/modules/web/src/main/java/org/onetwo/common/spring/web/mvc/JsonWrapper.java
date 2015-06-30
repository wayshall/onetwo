package org.onetwo.common.spring.web.mvc;

import java.io.Serializable;

/*****
 * @author wayshall
 *
 */
@SuppressWarnings("serial")
public class JsonWrapper implements Serializable{
	
	public static JsonWrapper wrap(Object value){
		return new JsonWrapper(value);
	}
	
	private Object value;

	public JsonWrapper(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}
	
}
