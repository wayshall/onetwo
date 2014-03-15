package org.onetwo.common.profiling;

public interface TimeLogger {

	TimeLogger INSTANCE = new Log4jTimeLogger();
	void log(String msg);

}