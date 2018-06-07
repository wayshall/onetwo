package org.onetwo.boot.limiter;

import org.onetwo.boot.limiter.InvokeContext.InvokeType;
import org.onetwo.boot.limiter.InvokeLimiter.TimesInvokeLimiter;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 每秒的速率
 * qps
 * @author wayshall
 * <br/>
 */
public class LocalRateLimiter extends TimesInvokeLimiter {

	private RateLimiter rateLimiter;
	
	public LocalRateLimiter(String key, Matcher matcher) {
		super(key, null, matcher);
	}
	
	@Override
	public void init() {
		this.setInvokeType(InvokeType.BEFORE);
		super.init();
		this.rateLimiter = RateLimiter.create(getLimitTimes());
	}

	@Override
	public void consume(InvokeContext invokeContext) {
//		log.info("times: {}", invokeContext.getInvokeTimes());
		if(!rateLimiter.tryAcquire(invokeContext.getInvokeTimes())){
			throw new LimitInvokeException(getKey(), getLimitTimes());
		}
	}
	
}
