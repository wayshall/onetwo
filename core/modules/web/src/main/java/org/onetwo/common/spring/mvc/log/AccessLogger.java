package org.onetwo.common.spring.mvc.log;

public interface AccessLogger {

	public void initLogger();
	public void logOperation(OperatorLogInfo info);
}
