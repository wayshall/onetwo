package org.onetwo.common.web.filter;

import java.util.concurrent.TimeUnit;

final public class RequestInfo {

	final private long startTime;
	
	public RequestInfo(long startTime) {
		super();
		this.startTime = startTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getExecutedTimeInMillis(){
		return System.currentTimeMillis() - startTime;
	}
	
	public long getExecutedTimeInSeconds(){
		return TimeUnit.MILLISECONDS.toSeconds(getExecutedTimeInMillis());
	}	
}
