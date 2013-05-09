package org.onetwo.common.utils;

import java.util.Date;

public class TimeCounter {

	private Object target;
	private Date start;
	private Date stop;
	private long costTime;

	public TimeCounter(Object target) {
		this.target = target;
	}

	@SuppressWarnings("deprecation")
	public Date start() {
		long start = System.currentTimeMillis();
		this.start = new Date(start);
		System.out.print(this.target + " ----->>> start time : " + this.start.toLocaleString());
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

	@SuppressWarnings("deprecation")
	public Date stop() {
		long stop = System.currentTimeMillis();
		this.stop = new Date(stop);
		this.costTime = this.stop.getTime() - this.start.getTime();
		System.out.print(this.target + " ----->>> stop time : " + this.start.toLocaleString()+" ");
		System.out.println(" cost total time : " + this.costTime+", (second): " + (this.costTime / 1000));
		return this.stop;
	}

}
