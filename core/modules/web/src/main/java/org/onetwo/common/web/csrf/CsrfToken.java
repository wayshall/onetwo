package org.onetwo.common.web.csrf;

public class CsrfToken {
	// private final String fieldOfFieldName;
	private final String fieldName;
	private final String value;

	protected CsrfToken(String fieldName, String value) {
		super();
		// this.fieldOfFieldName = fieldOfFieldName;
		this.fieldName = fieldName;
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getValue() {
		return value;
	}

}
