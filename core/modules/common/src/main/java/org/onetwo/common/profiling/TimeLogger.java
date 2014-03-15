package org.onetwo.common.profiling;

public interface TimeLogger {

	TimeLogger INSTANCE = new Log4jTimeLogger(TimeLogger.class);
	void log(String msg);

}