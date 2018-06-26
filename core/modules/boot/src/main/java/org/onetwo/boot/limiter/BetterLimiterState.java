package org.onetwo.boot.limiter;

import java.io.Serializable;
import java.util.concurrent.atomic.LongAdder;

import lombok.ToString;

/**
 * 使用并发性能更好的LongAdder实现
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("serial")
@ToString
public class BetterLimiterState implements LimiterState, Serializable {
	/***
	 * 重置时刻
	 */
	volatile private long resetAt;
	/***
	 * 速率
	 */
	final private int limitTimes;
	/***
	 * 剩余次数
	 */
	final private LongAdder currentTimes;
	final private long intervalInMillis;
	

	public BetterLimiterState(long intervalInMillis, int limitTimes) {
		super();
		this.limitTimes = limitTimes;
		this.intervalInMillis = intervalInMillis;
		this.currentTimes = new LongAdder();
		this.resetAt = System.currentTimeMillis();
	}

	
	public boolean isAllow(){
		long now = System.currentTimeMillis();
		if(now>=resetAt+intervalInMillis){
			this.currentTimes.reset();
			this.resetAt = now;
		}
		long value = currentTimes.longValue();
		if(value<limitTimes){
			currentTimes.increment();
			return true;
		}else{
			return false;
		}
	}
	
}
