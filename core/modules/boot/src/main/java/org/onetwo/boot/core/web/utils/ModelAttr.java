package org.onetwo.boot.core.web.utils;

public class ModelAttr {
	public static final String MESSAGE = "message";
	public static final String ERROR_MESSAGE = "error";
	
	private String name;
	private Object value;
	
	public ModelAttr() {
		super();
	}
	public ModelAttr(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}

}
