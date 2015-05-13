package org.onetwo.common.utils;

import java.io.Serializable;


public interface Result<CODE, DATA> extends Serializable {

	public CODE getCode();
	public String getMessage();
	public DATA getData();
	
}
