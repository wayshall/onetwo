package org.onetwo.common.profiling;

import java.util.Date;

import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;

public class TimeCounter {

	private Object target;
	private Date start;
	private Date stop;
	private long costTime;
	private boolean printMemory;


	public TimeCounter(Object target) {
		this(target, false);
	}
	public TimeCounter(Object target, boolean printMemory) {
		this.target = target;
		this.printMemory = printMemory;
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
		String msg = this.target + " ----->>> start time[" + DateUtil.formatDateTimeMillis(start)
				+ "], stop time[" + DateUtil.formatDateTimeMillis(this.stop)
				+ "], cost time[" + this.costTime+" (millis), " + (this.costTime / 1000) + " (second)]";
		if(printMemory){
			msg += "\n" + LangUtils.statisticsMemory("");
		}
		System.out.println(msg);
		return this.stop;
	}
	
	public void printMemory(){
		LangUtils.printMemory();
		System.out.println();
	}

}
