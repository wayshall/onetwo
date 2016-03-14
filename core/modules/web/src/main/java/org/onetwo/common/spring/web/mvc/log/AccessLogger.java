package org.onetwo.common.spring.web.mvc.log;

public interface AccessLogger {

	public void initLogger();
	public void logOperation(OperatorLogInfo info);
}
