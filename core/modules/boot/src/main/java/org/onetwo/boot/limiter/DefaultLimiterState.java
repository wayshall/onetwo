package org.onetwo.boot.limiter;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wayshall
 * <br/>
 */
@SuppressWarnings("serial")
public class DefaultLimiterState implements LimiterState, Serializable {
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
	final private AtomicInteger remainTimes;
	final private int intervalInMillis;
	

	public DefaultLimiterState(int limitTimes, int intervalInMillis) {
		super();
		this.limitTimes = limitTimes;
		this.intervalInMillis = intervalInMillis;
		this.remainTimes = new AtomicInteger(limitTimes);
		this.resetAt = System.currentTimeMillis();
	}

	
	public boolean isAllow(){
		long now = System.currentTimeMillis();
		if(now>=resetAt+intervalInMillis){
			this.remainTimes.set(limitTimes);
			this.resetAt = now;
		}
//		int remain = remainTimes.accumulateAndGet(1, (pre, val)->pre-val);
		int remain = remainTimes.getAndDecrement();
		return remain>0;
	}
	
}
