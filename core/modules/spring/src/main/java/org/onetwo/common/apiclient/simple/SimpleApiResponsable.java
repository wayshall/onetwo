package org.onetwo.common.apiclient.simple;

import org.onetwo.common.annotation.IgnoreField;
import org.onetwo.common.apiclient.ApiResponsable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
public class SimpleApiResponsable implements ApiResponsable<Object> {
	
	private boolean success;
	private Object resultCode;
	private String resultMessage;

	@Override
	@IgnoreField
	@JsonIgnore
	public boolean isSuccess() {
		return success;
	}

	@Override
	public Object resultCode() {
		return resultCode;
	}

	@Override
	public String resultMessage() {
		return resultMessage;
	}
	
}
