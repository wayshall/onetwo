package org.onetwo.common.db.sqlext;

public enum SQLKeys {

	Null("null", null),
	Empty("", "");

	private String value;
	private Object javaValue;
	
	SQLKeys(String value, Object javaValue){
		this.value = value;
		this.javaValue = javaValue;
	}

	public String getValue() {
		return value;
	}

	public Object getJavaValue() {
		return javaValue;
	}
	
}
