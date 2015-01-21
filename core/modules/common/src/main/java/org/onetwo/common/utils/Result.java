package org.onetwo.common.utils;


public interface Result<CODE, DATA> {

	public CODE getCode();
	public String getMessage();
	public DATA getData();
	
}
