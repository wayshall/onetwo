package org.onetwo.common.profiling;

public interface TimeLogger {

	TimeLogger INSTANCE = new Slf4jTimeLogger();
	void log(Class<?> logSource, String msg, Object...args);

}