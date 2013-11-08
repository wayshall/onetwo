package org.onetwo.common.profiling;

public interface TimeLogger {
	
	public static final String PROFILE_LOGGER = "profileLogger";

	void log(String msg);

}