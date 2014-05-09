package org.onetwo.common.profiling;

public interface JFishLogger {

	JFishLogger INSTANCE = new Log4jTimeLogger(JFishLogger.class);
	void log(String msg);

}