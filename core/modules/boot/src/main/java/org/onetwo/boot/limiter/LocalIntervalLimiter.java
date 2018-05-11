package org.onetwo.boot.limiter;

import java.util.Optional;

import org.onetwo.boot.limiter.InvokeContext.InvokeType;
import org.onetwo.boot.limiter.InvokeLimiter.BaseInvokeLimiter;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangOps;

/**
 * qps
 * @author wayshall
 * <br/>
 */
public class LocalIntervalLimiter extends BaseInvokeLimiter {
	public static final String START_KEY = LocalIntervalLimiter.class.getName()+"_START_TIME";
	private String interval;
	private long intervalInMillis = 1000*60;
	
	@Override
	public void init() {
		super.init();
		this.setInvokeType(InvokeType.ALL);
		this.intervalInMillis = LangOps.timeToMills(interval, intervalInMillis);
	}

	@Override
	public void consume(InvokeContext invokeContext) {
		if(invokeContext.getInvokeType()==InvokeType.BEFORE){
			invokeContext.setAttribute(START_KEY, System.currentTimeMillis());
		}else{
			Optional<Long> startMillisOpt = invokeContext.getAttributeOpt(START_KEY);
			if(!startMillisOpt.isPresent()){
				throw new BaseException("start time not found!");
			}
			Long costTime = System.currentTimeMillis() - startMillisOpt.get();
		}
	}
	
}
