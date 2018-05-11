package org.onetwo.boot.limiter;

import org.onetwo.boot.limiter.InvokeContext.InvokeType;
import org.onetwo.boot.limiter.InvokeLimiter.BaseInvokeLimiter;

import com.google.common.util.concurrent.RateLimiter;

/**
 * qps
 * @author wayshall
 * <br/>
 */
public class LocalRateLimiter extends BaseInvokeLimiter {
	private RateLimiter rateLimiter;
	
	@Override
	public void init() {
		super.init();
		this.setInvokeType(InvokeType.BEFORE);
		this.rateLimiter = RateLimiter.create(getLimitTimes());
	}

	@Override
	public void consume(InvokeContext invokeContext) {
//		log.info("times: {}", invokeContext.getInvokeTimes());
		if(!rateLimiter.tryAcquire(invokeContext.getInvokeTimes())){
			throw new LimitInvokeException(getLimitTimes());
		}
	}
	
}
