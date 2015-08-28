package org.onetwo.common.profiling;

import java.util.Date;

import org.onetwo.common.date.DateUtil;


public class TimerOutputer implements JFishLogger {
	
	@Override
	public void log(String msg){
		System.out.println("["+DateUtil.formatDateTime(new Date())+"]: "+msg);
	}

	@Override
	public void log(Object logSource, String msg) {
		System.out.println("["+DateUtil.formatDateTime(new Date())+"]: "+logSource.getClass().getSimpleName()+" - "+msg);
	}

}
