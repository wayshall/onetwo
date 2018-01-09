package org.onetwo.common.data;

import java.io.Serializable;


public interface Result extends Serializable {
	public String getCode();
	public String getMessage();
	public boolean isSuccess();
}
