package org.onetwo.common.profiling;

import java.util.Date;

import org.onetwo.common.utils.DateUtil;

public class TimeCounter {

	private Object target;
	private Date start;
	private Date stop;
	private long costTime;

	public TimeCounter(Object target) {
		this.target = target;
	}

	public Date start() {
		long start = System.currentTimeMillis();
		this.start = new Date(start);
		return this.start;
	}
	

	
	public Date restart() {
		return restart(target);
	}
	
	public Date restart(Object target) {
		System.out.print("restart...");
		this.target = target;
		return start();
	}

	public Date stop() {
		long stopMills = System.currentTimeMillis();
		this.stop = new Date(stopMills);
		this.costTime = this.stop.getTime() - this.start.getTime();
		System.out.println(this.target + " ----->>> start time[" + DateUtil.formatDateTimeMillis(start)
				+ "], stop time[" + DateUtil.formatDateTimeMillis(this.stop)
				+ "], cost time[" + this.costTime+" (millis), " + (this.costTime / 1000) + " (second)]");
		return this.stop;
	}

}
