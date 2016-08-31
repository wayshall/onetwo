package org.onetwo.ext.security.log;


public interface ActionLogHandler<T> {

	void saveLog(T actionLog);

}
