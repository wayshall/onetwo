package org.onetwo.common.result;

import java.io.Serializable;


public interface Result<CODE, DATA> extends Serializable {

	public CODE getCode();
	public String getMessage();
	public DATA getData();
	
}
