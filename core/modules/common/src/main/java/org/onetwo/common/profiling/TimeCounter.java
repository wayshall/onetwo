package org.onetwo.common.profiling;

import java.util.Date;
import java.util.function.Consumer;

import org.onetwo.common.date.DateUtils;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.slf4j.helpers.NOPLogger;

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
    private TimeLogger timeLogger;


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
	
	protected TimeLogger getTimeLogger(){
		TimeLogger logger = this.timeLogger;
		if(logger==null){
			Slf4jTimeLogger slf4j = new Slf4jTimeLogger();
			if(slf4j.getLogger() instanceof NOPLogger){
				logger = new TimerOutputer();
			}else{
				logger = slf4j;
			}
			this.timeLogger = logger;
		}
		return logger;
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
		this.message = new StringBuilder();
		this.target = target;
		return start();
	}

	public Date stop() {
		return stop(true);
	}
	public Date stop(boolean printMessage) {
		return stop(printMessage?msg->getTimeLogger().log(this.getClass(), msg):null);
	}
	public Date stop(Consumer<String> printer) {
		long stopMills = System.currentTimeMillis();
		this.stop = new Date(stopMills);
		this.costTime = this.stop.getTime() - this.start.getTime();
		if(printer!=null){
			message.append(this.target)
					.append("cost time[").append(this.costTime).append(" (millis), ").append(this.costTime / 1000).append(" (second)]")
					.append(", start time[").append(DateUtils.formatDateTimeMillis(start))
					.append("], stop time[").append(DateUtils.formatDateTimeMillis(this.stop))
					.append("]");
			if(printMemory){
				message.append("\n").append(LangUtils.statisticsMemory(""));
			}
			printer.accept(message.toString());
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
