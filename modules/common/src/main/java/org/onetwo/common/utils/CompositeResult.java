package org.onetwo.common.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class CompositeResult implements Serializable {
	public static final int FAILED = -1;
	
	private Object value;
	private List<String> messages;
	private int statusCode = 1;
	
	public CompositeResult(){
	}
	
	public CompositeResult(Object value){
		this.value = value;
	}
	
	public boolean isFailed(){
		return FAILED == getStatusCode();
	}
	
	public void failed(){
		setStatusCode(FAILED);
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void addMessages(String message) {
		if(this.messages==null)
			this.messages = new ArrayList<String>();
		this.messages.add(message);
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	

	
}
