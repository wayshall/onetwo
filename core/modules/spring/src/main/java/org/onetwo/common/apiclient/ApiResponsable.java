package org.onetwo.common.apiclient;

import org.onetwo.common.annotation.IgnoreField;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author weishao zeng
 * <br/>
 */
public interface ApiResponsable<T> {
	
	/***
	 * 相应是否成功
	 * @author weishao zeng
	 * @return
	 */
	@IgnoreField
	@JsonIgnore
	default boolean isSuccess() {
		T code = resultCode();
		return "success".equalsIgnoreCase(code.toString()) ||
				Integer.valueOf(0).equals(code) ||
				"ok".equalsIgnoreCase(code.toString());
	}

	T resultCode();

	String resultMessage();
	
	
}

