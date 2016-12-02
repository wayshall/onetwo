package org.onetwo.common.profiling;

import java.util.Date;

import org.onetwo.common.date.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;

public class TimeCounter {

	public static TimeCounter create(Object target, TimeLogger timeLogger){
		TimeCounter t = new TimeCounter(target);
		t.logger(timeLogger);
		return t;
	}
	public static TimeCounter start(Object target){
		TimeCounter t = new TimeCounter(target);
		t.start();
		return t;
	}

	private Object target;
	private Date start;
	private Date stop;
	private long costTime;
	private boolean printMemory;
	private StringBuilder message = new StringBuilder();
    private TimeLogger timeLogger = TimeLogger.INSTANCE;


	public TimeCounter(Object target) {
		this(target, false);
	}
	public TimeCounter(Object target, Logger logger) {
		super();
		this.target = target;
		this.timeLogger = new Slf4jTimeLogger(logger);
	}
	public TimeCounter(Object target, boolean printMemory) {
		this.target = target;
		this.printMemory = printMemory;
	}

	
	public TimeCounter logger(TimeLogger timeLogger){
		this.timeLogger = timeLogger;
		return this;
	}

	public Date start() {
		if(printMemory){
			message.append(LangUtils.statisticsMemory(""));
		}
		
		long start = System.currentTimeMillis();
		this.start = new Date(start);
		return this.start;
	}

	public TimeCounter startIt() {
		start();
		return this;
	}
	

	
	public Date restart() {
		return restart(target);
	}
	
	public Date restart(Object target) {
		timeLogger.log(this.getClass(), "restart time counter...");
		this.message = new StringBuilder();
		this.target = target;
		return start();
	}

	public Date stop() {
		return stop(true);
	}
	public Date stop(boolean printMessage) {
		long stopMills = System.currentTimeMillis();
		this.stop = new Date(stopMills);
		this.costTime = this.stop.getTime() - this.start.getTime();
		message.append(this.target)
				.append("---> cost time[").append(this.costTime).append(" (millis), ").append(this.costTime / 1000).append(" (second)]")
				.append(", start time[").append(DateUtil.formatDateTimeMillis(start))
				.append("], stop time[").append(DateUtil.formatDateTimeMillis(this.stop))
				.append("]");
		if(printMemory){
			message.append("\n").append(LangUtils.statisticsMemory(""));
		}
		if(printMessage){
			timeLogger.log(this.getClass(), message.toString());
		}
		return this.stop;
	}
	
	public String getMessage() {
		return message.toString();
	}
	public void printMemory(){
		LangUtils.printMemory();
		System.out.println();
	}

}
