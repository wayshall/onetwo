package org.onetwo.boot.limiter;

import java.util.concurrent.TimeUnit;

import org.onetwo.boot.limiter.InvokeContext.InvokeType;
import org.onetwo.boot.limiter.InvokeLimiter.BaseInvokeLimiter;
import org.onetwo.common.utils.LangOps;

/**
 * 即自定义时间的tps
 * @author wayshall
 * <br/>
 */
public class LocalIntervalLimiter extends BaseInvokeLimiter {
	private String interval;
	private long intervalInMillis = 1000*60;
	private LimiterState limiterState;
	
	public void setInterval(int time, TimeUnit timeUnit){
		this.intervalInMillis = timeUnit.toMillis(time);
	}
	
	@Override
	public void init() {
		super.init();
		this.setInvokeType(InvokeType.BEFORE);
		this.intervalInMillis = (int)LangOps.timeToMills(interval, intervalInMillis);
		
		this.limiterState = new DefaultLimiterState(intervalInMillis, getLimitTimes());
	}

	@Override
	public void consume(InvokeContext invokeContext) {
		if(!limiterState.isAllow()){
			throw new LimitInvokeException(getLimitTimes());
		}
	}
	
}
