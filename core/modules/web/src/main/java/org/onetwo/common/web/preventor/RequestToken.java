package org.onetwo.common.web.preventor;

import java.io.Serializable;

public class RequestToken implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4292992141604008190L;
	// private final String fieldOfFieldName;
//	private final MDEncrypt encrypt;
	private final String fieldName;
	private final String value;

	public RequestToken(String fieldName, String value) {
		super();
		// this.fieldOfFieldName = fieldOfFieldName;
//		this.encrypt = encrypt;
		this.fieldName = fieldName;
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getValue() {
		return value;
	}
	
	public String getGeneratedValue(TokenValueGenerator generator){
//		return encrypt.encryptWithSalt(value);
		return generator.generatedTokenValue(this);
	}

}
