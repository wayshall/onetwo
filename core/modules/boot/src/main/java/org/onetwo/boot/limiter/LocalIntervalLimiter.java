package org.onetwo.boot.limiter;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.onetwo.boot.limiter.InvokeContext.InvokeType;
import org.onetwo.boot.limiter.InvokeLimiter.TimesInvokeLimiter;
import org.onetwo.common.utils.LangOps;

/**
 * 即自定义时间的tps
 * @author wayshall
 * <br/>
 */
public class LocalIntervalLimiter extends TimesInvokeLimiter {

	private String interval;
	private long intervalInMillis = 1000*60;
	private LimiterState limiterState;
	private Function<LocalIntervalLimiter, LimiterState> limiterStateCreator;

	public LocalIntervalLimiter(String key, Matcher matcher) {
		super(key, null, matcher);
	}
	
	public void setInterval(int time, TimeUnit timeUnit){
		this.intervalInMillis = timeUnit.toMillis(time);
	}
	
	@Override
	public void init() {
		this.setInvokeType(InvokeType.BEFORE);
		super.init();
		this.intervalInMillis = (int)LangOps.timeToMills(interval, intervalInMillis);
		
		if(limiterStateCreator==null){
			this.limiterState = new DefaultLimiterState(intervalInMillis, getLimitTimes());
		}else{
			this.limiterState = limiterStateCreator.apply(this);
		}
	}

	@Override
	public void consume(InvokeContext invokeContext) {
		if(!limiterState.isAllow()){
			throw new LimitInvokeException(getKey(), getLimitTimes());
		}else{
			if(logger.isDebugEnabled()){
				logger.debug("consumed, current limiter state: {}", limiterState);
			}
//			System.out.println(Thread.currentThread().getName()+": consumed..., state: " + limiterState);
		}
	}

	public long getIntervalInMillis() {
		return intervalInMillis;
	}

	public void setLimiterStateCreator(Function<LocalIntervalLimiter, LimiterState> limiterStateCreator) {
		this.limiterStateCreator = limiterStateCreator;
	}
	
}
