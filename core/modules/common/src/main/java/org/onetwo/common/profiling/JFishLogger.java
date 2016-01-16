package org.onetwo.common.profiling;

public interface JFishLogger {

	JFishLogger INSTANCE = new Slf4jTimeLogger();
	void log(String msg);
	void log(Object logSource, String msg);

}