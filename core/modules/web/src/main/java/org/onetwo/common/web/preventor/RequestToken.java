package org.onetwo.common.web.preventor;

import org.onetwo.common.utils.encrypt.MDEncrypt;

public class RequestToken {
	// private final String fieldOfFieldName;
	private final MDEncrypt encrypt;
	private final String fieldName;
	private final String value;

	public RequestToken(MDEncrypt encrypt, String fieldName, String value) {
		super();
		// this.fieldOfFieldName = fieldOfFieldName;
		this.encrypt = encrypt;
		this.fieldName = fieldName;
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getValue() {
		return value;
	}
	
	public String getGeneratedValue(){
		return encrypt.encryptWithSalt(value);
	}

}
