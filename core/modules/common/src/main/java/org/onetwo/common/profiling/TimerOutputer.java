package org.onetwo.common.profiling;

import java.util.Date;

import org.onetwo.common.date.DateUtils;


public class TimerOutputer implements TimeLogger {

	@Override
	public void log(Class<?> logSource, String msg, Object...args) {
		System.out.println("["+DateUtils.formatDateTime(new Date())+"]: "+logSource.getClass().getSimpleName()+" - "+msg);
	}

}
